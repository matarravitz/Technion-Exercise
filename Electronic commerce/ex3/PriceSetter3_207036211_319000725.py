import numpy as np
from scipy.optimize import minimize_scalar
from scipy.stats import beta as betaD
from time import perf_counter


class PriceSetter3:
    def __init__(self, rounds):

        self.rounds = rounds
        self.alpha_posterior_a = 1  # Initial values for alpha posterior
        self.alpha_posterior_b = 1  # Initial values for beta posterior
        self.beta_posterior_a = 1  # Initial values for alpha posterior
        self.beta_posterior_b = 1  # Initial values for beta posterior
        self.currValue = None
        self.rightBorder = 1
        self.leftBorder = 0

    def expected_profit(self, p):

        cdf_p = betaD.cdf(p, self.alpha_posterior_a, self.beta_posterior_b)
        return -p * (1 - cdf_p)

    def set_price(self, t):

        # Sample alpha and beta from the posterior distributions
        self.alpha = np.random.beta(self.alpha_posterior_a, self.alpha_posterior_b)
        self.beta_params = np.random.beta(self.beta_posterior_a, self.beta_posterior_b)

        # Minimize the expected profit to find the optimal price
        result = minimize_scalar(self.expected_profit, bounds=(self.leftBorder, self.rightBorder), method='bounded')
        self.currValue = result.x

        return self.currValue

    def update(self, t, outcome):

        if outcome == 1:
            self.alpha_posterior_a += 1
            self.leftBorder = self.currValue
        else:
            self.beta_posterior_b += 1
            self.rightBorder = self.currValue
