from time import perf_counter
import numpy as np
from scipy.stats import beta as betaD
from scipy.optimize import minimize_scalar

class PriceSetter2:
    def __init__(self, rounds, alpha, beta_params):

        self.rounds = rounds
        self.alpha = alpha
        self.beta_params = beta_params
        self.currValue = None
        self.rightBorder = 1
        self.leftBorder = 0
        self.result = minimize_scalar(self.expected_profit, bounds=(self.leftBorder, self.rightBorder), method='bounded')

    def expected_profit(self, p):
        cdf_p = betaD.cdf(p, self.alpha, self.beta_params)
        return -p * (1 - cdf_p)

    def set_price(self, t):
        self.currValue = self.result.x
        return self.currValue

    def update(self, t, outcome):
        if outcome == 1:
            self.currValue = self.currValue
            self.leftBorder = self.currValue
        else:
            self.currValue = self.currValue

            self.rightBorder = self.currValue
