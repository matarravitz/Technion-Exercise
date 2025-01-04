import networkx as nx
import random
import pandas as pd
import numpy as np

NoseBook_path = 'NoseBook_friendships.csv'
cost_path = 'costs.csv'

def P_product_exposure_score(net, purchased_set):
    exposure = 0
    for user in net.nodes:
        neighborhood = set(net.neighbors(user))
        if user in purchased_set:
            exposure += 1
        elif len(neighborhood.intersection(purchased_set)) != 0:
            b = len(neighborhood.intersection(purchased_set))
            exposure += 1 / (1 + 10 * np.exp(-b / 2))
    return exposure

def IC(S_influencers, net, num_simulations):
    total_exposure = 0
    for _ in range(num_simulations):
        buyers = set(S_influencers)
        for _ in range(6):
            buyers = buy_products(net, buyers)
        total_exposure += P_product_exposure_score(net, buyers)
    return total_exposure / num_simulations


def create_graph(edges_path: str) -> nx.Graph:
    edges = pd.read_csv(edges_path).to_numpy()
    net = nx.Graph()
    net.add_edges_from(edges)
    return net

def buy_products(net: nx.Graph, purchased: set) -> set:
    new_purchases = set()
    for user in net.nodes:
        neighborhood = set(net.neighbors(user))
        b = len(neighborhood.intersection(purchased))
        n = len(neighborhood)
        prob = b / n
        if prob >= random.uniform(0, 1):
            new_purchases.add(user)
    return new_purchases.union(purchased)

def product_exposure_score(net: nx.Graph, purchased_set: set) -> int:
    exposure = 0
    for user in net.nodes:
        neighborhood = set(net.neighbors(user))
        if user in purchased_set:
            exposure += 1
        elif len(neighborhood.intersection(purchased_set)) != 0:
            b = len(neighborhood.intersection(purchased_set))
            rand = random.uniform(0, 1)
            if rand < 1 / (1 + 10 * np.exp(-b / 2)):
                exposure += 1
    return exposure

def neighborhood_overlap(G, node, selected_nodes):
    """
    Calculate the average neighborhood overlap between a node and a group of nodes.
    We actually dont use it, but in some of our trials we used it in order to minimize the overlap between different candidates
    """
    overlap_sum = 0
    for selected_node in selected_nodes:
        neighbors_node = set(G.neighbors(node))
        neighbors_selected_node = set(G.neighbors(selected_node))
        intersection = len(neighbors_node & neighbors_selected_node)
        union = len(neighbors_node | neighbors_selected_node)
        overlap = intersection / union if union != 0 else 0
        overlap_sum += overlap
    return overlap_sum / len(selected_nodes) if selected_nodes else 0

def get_influencers_cost(cost_path: str, influencers: list) -> int:
    costs = pd.read_csv(cost_path)
    return sum(
        [costs[costs['user'] == influencer]['cost'].item() if influencer in list(costs['user']) else 0 for influencer in
         influencers])

def get_user_cost(cost_path: str, user) -> int:
    costs = pd.read_csv(cost_path)
    return costs[costs['user'] == user]['cost'].item() if user in list(costs['user']) else 0

def select_influencers(G, K):
    """
    Select K influencers based on centrality and minimal neighborhood overlap.
    Note it is a first selection of the influencers and not the final group
    """
    # Step 1: Calculate betweenness centrality
    betweenness = nx.betweenness_centrality(G)

    # Step 2: Sort nodes by betweenness centrality in descending order and select top K
    sorted_by_betweenness = sorted(betweenness.items(), key=lambda x: x[1], reverse=True)[:K]
    top_k_betweenness_nodes = [node for node, _ in sorted_by_betweenness]

    # Step 3: Calculate degree centrality for the top K nodes by betweenness
    degree = nx.degree_centrality(G)
    top_k_degree_nodes = sorted(list(top_k_betweenness_nodes), key=lambda x: degree[x], reverse=True)[:K // 2]

    return top_k_degree_nodes

def climbing_Hill():
    """
    The main function that determines which are the final influencers we chose.This function
    uses a submodular function(The Roi function) in order to maximize the ration between the influence of the
    influencers and the cost of each one of them in order to choose who will be the next candidate to enter our list
    :return: list of influencers
    """
    net = create_graph(NoseBook_path)
    S_influencers = set()
    prev_IC = 0
    k=400
    top_k = select_influencers(net, k)
    curr_candidates = set(top_k)
    num_of_simulation = 100
    flag=True
    while flag:
        candidate = None  # Reset candidate at the beginning of each loop
        false_counter = 0
        best_score=0
        for user in curr_candidates:
            temp_arr = list(S_influencers.union({user}))
            temp_cost = get_influencers_cost(cost_path, temp_arr)
            if temp_cost <= 1000:
                new_IC = IC(S_influencers.union({user}), net, num_of_simulation)
                m_user = new_IC - prev_IC #the difference between the previous IC to the new IC with a new node
                curr_user_cost = get_user_cost(cost_path, user)
                score = m_user / curr_user_cost  # ROI score
                if score > best_score:
                    best_score = score
                    candidate = user
            else:
                false_counter += 1
        if false_counter == len(curr_candidates):
            flag=False
        else :
            S_influencers=S_influencers.union({candidate})
            curr_candidates=curr_candidates.difference({candidate})
            prev_IC = IC(S_influencers, net, num_of_simulation)
    seed_arr = list(S_influencers)
    return seed_arr

def simulate_influence_scoring(net, influencers, num_simulations):
    """
    function in order to calculate the average score of the influencers we found
    :param net: the nosebook network
    :param influencers: the influencers we want to estimate their influence
    :param num_simulations: number of simulations in order to estimate their influence
    :return: the average score of the group
    """
    total_score = 0
    for _ in range(num_simulations):
        purchased = set(influencers)

        for i in range(6):  # Simulate 6 rounds of product exposure
            purchased = buy_products(net, purchased)

        score = product_exposure_score(net, purchased)
        total_score += score

    average_score = total_score / num_simulations
    return average_score

if __name__ == '__main__':
    influencers = climbing_Hill()
    print(influencers)
