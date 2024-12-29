import numpy as np
import pandas as pd
from scipy.sparse import csr_matrix, vstack, eye
from scipy.sparse.linalg import lsqr

#Q1

# reading the data
train_data = pd.read_csv('user_clip.csv')
test_data = pd.read_csv('test.csv')

# computing the avg
r_avg = train_data['weight'].mean()

unique_users = train_data['user_id'].unique()
unique_items = train_data['clip_id'].unique()
num_users = len(unique_users)
num_items = len(unique_items)

user_to_index = {user_id: idx for idx, user_id in enumerate(unique_users)}
item_to_index = {item_id: idx for idx, item_id in enumerate(unique_items)}


user_indices = train_data['user_id'].map(user_to_index).values
item_indices = train_data['clip_id'].map(item_to_index).values
weights = train_data['weight'].values

# Building a sparse matrix for the least square
n_samples = len(weights)
data = np.ones(n_samples)
row_indices = np.arange(n_samples)
col_indices_users = user_indices
col_indices_items = item_indices + num_users

A_user = csr_matrix((data, (row_indices, col_indices_users)), shape=(n_samples, num_users + num_items))
A_item = csr_matrix((data, (row_indices, col_indices_items)), shape=(n_samples, num_users + num_items))
A = A_user + A_item

# solving the problem
b = weights - r_avg
reg_param = 0.1
reg_matrix = reg_param * eye(num_users + num_items, format='csr')
A = vstack([A, reg_matrix])
b = np.concatenate([b, np.zeros(num_users + num_items)])

result = lsqr(A, b)
params = result[0]
b_u = params[:num_users]
b_i = params[num_users:num_users + num_items]

# predicting the results
def predict_rating_final(user_id, clip_id):
    user_idx = user_to_index.get(user_id, -1)
    item_idx = item_to_index.get(clip_id, -1)
    if item_idx == -1 and user_idx == -1:
        bu = 0
        bi = 0
    elif user_idx == -1 and item_idx != -1:
        bu = 0
        bi = b_i[item_idx]
    elif item_idx == -1 and user_idx != -1:
        bu = b_u[user_idx]
        bi = 0
    else:
        bu = b_u[user_idx]
        bi = b_i[item_idx]
    score = r_avg + bu + bi
    if score > 0:
        return score
    else:
        return 0

test_data['weight'] = test_data.apply(lambda row: predict_rating_final(row['user_id'], row['clip_id']), axis=1)

# computing the f_1:
SSE = 0
regularization_u = np.sum(b_u ** 2)
regularization_i = np.sum(b_i ** 2)

for row in train_data.itertuples():
    user_id = row.user_id
    clip_id = row.clip_id
    weight = row.weight
    r_u_i_prediction = predict_rating_final(user_id, clip_id)
    squared_error = (weight - r_u_i_prediction) ** 2
    SSE += squared_error

f_1 = SSE + reg_param * (regularization_u + regularization_i)

output_data = test_data[['user_id', 'clip_id', 'weight']]

output_file_path = '207036211_319000725_task1.csv'
output_data.to_csv(output_file_path, index=False)

#Q2

import numpy as np
import pandas as pd
from scipy.sparse.linalg import svds

train_data = pd.read_csv('user_clip.csv')
test_data = pd.read_csv('test.csv')

R = train_data.pivot(index='user_id', columns='clip_id', values='weight')
R.fillna(0, inplace=True)

user_indices = R.index
clip_indices = R.columns

R_matrix = R.to_numpy()

U, sigma, Vt = svds(R_matrix, k=20)
sigma = np.diag(sigma)

R_hat = np.dot(np.dot(U, sigma), Vt)

R_hat_df = pd.DataFrame(R_hat, index=user_indices, columns=clip_indices)


def predict_rating(user_id, clip_id):
    value = R_hat_df.loc[user_id, clip_id]
    if value < 0:
        return 0
    return value


test_data['weight'] = test_data.apply(lambda row: predict_rating(row['user_id'], row['clip_id']), axis=1)
train_data['weight_prediction'] = train_data.apply(lambda row: predict_rating(row['user_id'], row['clip_id']), axis=1)

SSE = 0
for row in train_data.itertuples():
    user_id = row.user_id
    clip_id = row.clip_id
    weight = row.weight
    r_u_i_prediction = row.weight_prediction

    squared_error = (weight - r_u_i_prediction) ** 2

    SSE += squared_error

output_data = test_data[['user_id', 'clip_id', 'weight']]

output_file_path = '207036211_319000725_task2.csv'
output_data.to_csv(output_file_path, index=False)