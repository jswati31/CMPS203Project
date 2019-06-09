# Lexer Parser for C code

import re
from rules import *


class Node(object):
    def __init__(self, rule, children):
        self.rule = rule
        self.children = children

    def __repr__(self):
        return str(self.rule.o1) + " -> " + str(self.children)

    def bracket_repr(self):
        outstr = "[" + str(self.rule.o1) + " "
        outstr += ' '.join(child.bracket_repr() for child in self.children)
        outstr += "]"
        return outstr


class Lexer(object):

    def __init__(self):
        self.tokens = None

    def tokenize(self, program, token_list):
        split_program = re.split("\s+", program)

        for token_l in list(token_list.keys()):
            temp = []
            for t in split_program:
                if isinstance(t, str):
                    split_string = t.split(token_list[token_l].char)
                    for s in split_string:
                        if len(s) != 0:
                            temp += [s]
                        temp += [token_list[token_l]]
                    temp.pop()
                else:
                    temp += [t]
            split_program = temp

        self.tokens = split_program

        return self.tokens

    def token_mapping(self, splittoken):
        if isinstance(splittoken, token):
            return splittoken
        elif re.fullmatch("[0-9]*", splittoken):
            return token("integer", splittoken)
        elif re.fullmatch("[a-zA-Z_][a-zA-Z0-9_]*", splittoken):
            return token("string", splittoken)
        else:
            raise Exception("Error in tokenization")


class Parser(object):
    def __init__(self):
        self.stack = []
        self.tree_stack = []

    def build_ast(self, lexer_token_list, parsing_syntax, symbol):

        while True:
            for syn in list(parsing_syntax.keys()):
                if len(parsing_syntax[syn].o2) <= len(self.stack):
                    for p_syn, p_stack in zip(parsing_syntax[syn].o2[::-1], self.stack[::-1]):
                        if not p_syn.match(p_stack):
                            break
                    else:
                        if parsing_syntax[syn] == parsing_syntax['declare_separator_base'] and self.stack[-2] != \
                                parsing_syntax['declare_expression']:
                            pass
                        else:
                            self.tree_stack = self.tree_stack[:-len(parsing_syntax[syn].o2)] + [
                                Node(parsing_syntax[syn], self.tree_stack[-len(parsing_syntax[syn].o2):])]
                            self.stack = self.stack[:-len(parsing_syntax[syn].o2)] + [parsing_syntax[syn].o1]
                            break
                else:
                    continue
            else:
                if not lexer_token_list: break
                else:
                    self.stack += [lexer_token_list[0]]
                    self.tree_stack += [lexer_token_list[0]]
                    lexer_token_list = lexer_token_list[1:]

        if self.stack == [symbol['Start']]:
            return self.tree_stack[0]
        else:
            raise Exception










