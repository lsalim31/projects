# valueIterationAgents.py
# -----------------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
#
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


# valueIterationAgents.py
# -----------------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
#
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).



import mdp, util

from learningAgents import ValueEstimationAgent
import collections

class ValueIterationAgent(ValueEstimationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A ValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp: mdp.MarkovDecisionProcess, discount = 0.9, iterations = 100):
        """
          Your value iteration agent should take an mdp on
          construction, run the indicated number of iterations
          and then act according to the resulting policy.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state, action, nextState)
              mdp.isTerminal(state)
        """
        self.mdp = mdp
        self.discount = discount
        self.iterations = iterations
        self.values = util.Counter() # A Counter is a dict with default 0
        self.runValueIteration()

    def runValueIteration(self):
        """
          Run the value iteration algorithm. Note that in standard
          value iteration, V_k+1(...) depends on V_k(...)'s.
        """
        
        problem = self.mdp
        setOfStates = problem.getStates()
        T = lambda s, a: problem.getTransitionStatesAndProbs(s, a)
        R = lambda s, a, s_next: problem.getReward(s, a, s_next)
        Actions = lambda s: problem.getPossibleActions(s)
        k = self.iterations
        gamma = self.discount
        V = util.Counter()
        while k > 0:
            V_new = util.Counter()
            for s in setOfStates:
                if len(Actions(s)) != 0:
                    V_new[s] = max([sum([s_and_prob[1] * (R(s, a, s_and_prob[0]) + gamma*V[s_and_prob[0]]) 
                        for s_and_prob in T(s, a)]) for a in Actions(s)])
                else:
                    V_new[s] = 0
            V = V_new
            k -= 1
        self.values = V

    def getValue(self, state):
        """
          Return the value of the state (computed in __init__).
        """
        return self.values[state]

    def computeQValueFromValues(self, state, action):
        """
          Compute the Q-value of action in state from the
          value function stored in self.values.
        """

        problem = self.mdp
        T = lambda s, a: problem.getTransitionStatesAndProbs(s, a)
        R = lambda s, a, s_next: problem.getReward(s, a, s_next)
        gamma = self.discount
        Q = sum([s_and_prob[1] * (R(state, action, s_and_prob[0]) + gamma * self.getValue(s_and_prob[0])) 
                for s_and_prob in T(state, action)])
        return Q

    def computeActionFromValues(self, state):
        """
          The policy is the best action in the given state
          according to the values currently stored in self.values.

          You may break ties any way you see fit.  Note that if
          there are no legal actions, which is the case at the
          terminal state, you should return None.
        """
        problem = self.mdp
        Actions = lambda s: problem.getPossibleActions(s)
        Q = lambda a: self.computeQValueFromValues(state, a)
        if len(Actions(state)) == 0:
            return None
        pi = max(Actions(state), key = Q)
        return pi

    def getPolicy(self, state):
        return self.computeActionFromValues(state)

    def getAction(self, state):
        "Returns the policy at the state (no exploration)."
        return self.computeActionFromValues(state)

    def getQValue(self, state, action):
        return self.computeQValueFromValues(state, action)
