import numpy as np
from time import perf_counter

class PriceSetter1:

    def __init__(self, rounds):
        self.Totalrounds = rounds
        self.currPrice = 0
        self.pPrev = 0
        self.firstFalse = 0
        self.step1 = 0.1
        self.step2 = 0.0108
        self.secondFalse = 0

    def set_price(self, t):
        return self.currPrice

    def update(self, t, outcome):

        if (t < self.Totalrounds * 0.1) and self.firstFalse < 2:
            if outcome == 1:
                self.pPrev = self.currPrice
                self.currPrice = self.pPrev + self.step1
            if outcome == 0:
                self.currPrice = self.pPrev
                if self.secondFalse == 0:
                    self.step1 = self.step2
                    self.secondFalse = self.secondFalse + 1
                self.firstFalse = self.firstFalse + 1

        if (t % 50) == 0 and self.firstFalse == 2:
            self.firstFalse = 1
            self.step1 = self.step1 * 0.03
