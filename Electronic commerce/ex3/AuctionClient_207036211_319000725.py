import copy
import numpy as np
from time import perf_counter


class AuctionClient:
    def __init__(self, value, clients_num, insurances_num):
        self.value = value
        self.clients_num = clients_num
        self.insurances_num = insurances_num
        self.bid = 0
        self.duration = 0
        self.bestPrice = 0
        self.bestDiff = 0

    def threeMax(self, a, b, c):
        x = a[0]
        y = b[0]
        z = c[0]
        if x >= y:
            tempMax = a
        else:
            tempMax = b
        if tempMax[0] >= z:
            pass
        if z >= tempMax[0]:
            tempMax = c
        return tempMax[1]

    def decide_bid(self, t, duration):

        self.duration = duration

        if self.bestPrice == -1:
            if self.duration >= 2:
                return self.value / 3
            else:
                return -1

        if t == 0 and duration >= 2.5:
            return self.value/3

        elif t == 0 and duration < 2.5:
            return -1


        if (self.clients_num - t == 1 ):
            a = [duration * (self.value - self.bestPrice), self.value]
            b = [duration * 0.5 * self.value, self.bestPrice] #with probability of 0.5
            c = [0.5 * self.value, -1]
            self.bid = self.threeMax(a, b, c)

        if (self.clients_num - t <= 0):
            if self.insurances_num - t > 2:
                if self.duration >= 0.9:# 0.8
                    return self.value
                else:
                    return -1
            else:
                if self.duration > 0.5:
                    return self.value
                else:
                    return -1

        else:
            if self.insurances_num < self.clients_num:
                if duration >= 1.1:
                    if (self.value - self.bestPrice) >= 0.4 and self.value >= 1.833 * self.bestPrice:
                        self.bid = self.value
                    else:
                        self.bid = -1
                else:
                    self.bid = -1
            elif self.insurances_num - t > 2:
                if duration >= 1.2:
                    if (self.value - self.bestPrice) >= 0.5 and self.value >= 1.714 * self.bestPrice:
                        self.bid = self.value
                    else:
                        self.bid = -1
                else:
                    self.bid = -1
            else:
                if duration >= 0.6:
                    if (self.value - self.bestPrice) >= 0.5 and self.value >= 6 * self.bestPrice:
                        self.bid = self.value
                    else:
                        self.bid = -1
                else:
                    self.bid = -1
        return self.bid

    def update(self, t, price):
        self.bestPrice = price