#  File containing rules for expressions


class token(object):
    def __init__(self, data, char=None, priority=None):
        self.data = data
        self.char = char
        self.priority = priority

    def match(self, t):
        if not isinstance(t, token) or self.data != t.data:
            return False
        if not self.char:
            return True
        return self.char == t.char

    def __eq__(self, other):
        return (self.data == other.data) and (self.char == other.char)

    def __repr__(self):
        return str(str(self.data) + " " + str(self.char))

    def bracket_repr(self):
        return self.data


class TokenDict:
    def __init__(self):
        self.token_dict = {}
        self.name = None

    def add_token(self, name, t):
        self.token_dict[name] = t
        self.name = name

    def get_token_dict(self):
        return self.token_dict


class SymbolTable:
    def __init__(self, name):
        self.name = name

    def __repr__(self):
        return self.name

    def match(self, s):
        if not isinstance(s, SymbolTable):
            return False
        return self.name == s.name


class ParserSyntax:
    def __init__(self, o1, o2, priority=None):
        self.o1 = o1
        self.o2 = o2
        self.priority = priority

    def __repr__(self):
        return str(self.o1) + " -> " + str(self.o2)


class SymbolDict:
    def __init__(self):
        self.sym_dict = {}
        self.name = None

    def add_sym(self, s):
        self.sym_dict[str(s)] = s
        self.name = s

    def get_sym_dict(self):
        return self.sym_dict


class ParserDict:
    def __init__(self):
        self.rule_dict = {}
        self.name = None

    def add_rule(self, name, rule):
        self.rule_dict[name] = rule
        self.name = name

    def get_rule_dict(self):
        return self.rule_dict


def token_list_generator():
    create_token_list = TokenDict()

    # comparison tokens
    create_token_list.add_token("equalto", token("compare", "=="))
    create_token_list.add_token("notequalto", token("compare", "!="))
    create_token_list.add_token("lessequalto", token("compare", "<="))
    create_token_list.add_token("greaterequalto", token("compare", ">="))
    create_token_list.add_token("lessthan", token("compare", "<"))
    create_token_list.add_token("greaterthan", token("compare", ">"))

    # boolean expression tokens
    create_token_list.add_token("booland", token("boolean", "&&"))
    create_token_list.add_token("boolor", token("boolean", "||"))

    # brackets
    create_token_list.add_token("curly_bracket", token("bracket", "{"))
    create_token_list.add_token("curly_close_bracket", token("bracket", "}"))
    create_token_list.add_token("open_paren", token("bracket", "("))
    create_token_list.add_token("close_paren", token("bracket", ")"))
    create_token_list.add_token("open_sq_bracket", token("bracket", "["))
    create_token_list.add_token("close_sq_bracket", token("bracket", "]"))

    # assignment
    create_token_list.add_token("equal", token("assignment", "="))

    # separators
    create_token_list.add_token("semicolon", token("semicolon", ";"))
    create_token_list.add_token("comma", token("comma", ","))

    # arithmetic
    create_token_list.add_token("minus", token("arith", "-"))
    create_token_list.add_token("add", token("arith", "+"))
    create_token_list.add_token("mult", token("arith", "*"))
    create_token_list.add_token("div", token("arith", "/"))
    create_token_list.add_token("mod", token("arith", "%"))

    # conditional
    create_token_list.add_token("if", token("conditional", "if"))
    create_token_list.add_token("else", token("conditional", "else"))
    create_token_list.add_token("while", token("conditional", "while"))
    create_token_list.add_token("for", token("conditional", "for"))

    # command
    create_token_list.add_token("return", token("command", "return"))
    create_token_list.add_token("print", token("command", "printf"))

    # type int
    create_token_list.add_token("type", token("type", "int"))

    return create_token_list.get_token_dict()


def symbol_generator():
    sym_gen = SymbolDict()
    sym_gen.add_sym(SymbolTable("Start"))
    sym_gen.add_sym(SymbolTable("Statements"))
    sym_gen.add_sym(SymbolTable("Statement"))
    sym_gen.add_sym(SymbolTable("Expression"))
    sym_gen.add_sym(SymbolTable("declare_separator"))
    sym_gen.add_sym(SymbolTable("declare_type"))
    sym_gen.add_sym(SymbolTable("declare_expression"))
    sym_gen.add_sym(SymbolTable("if"))
    sym_gen.add_sym(SymbolTable("else"))
    sym_gen.add_sym(SymbolTable("while"))
    sym_gen.add_sym(SymbolTable("for_expr"))
    sym_gen.add_sym(SymbolTable("func_dec"))
    sym_gen.add_sym(SymbolTable("func_def"))
    sym_gen.add_sym(SymbolTable("func_call_start"))

    return sym_gen.get_sym_dict()


def parser_gen(symbol, tokens_list):
    parser = ParserDict()

    # main function declaration expression
    parser.add_rule("main_func_dec_cont", ParserSyntax(symbol["Start"], [symbol["Start"], symbol["func_dec"]]))
    parser.add_rule("main_func_def_cont", ParserSyntax(symbol['Start'], [symbol['Start'], symbol['func_def']]))
    parser.add_rule("main_function_declaration", ParserSyntax(symbol['Start'], [symbol['func_dec']]))
    parser.add_rule("main_function_definition",ParserSyntax(symbol['Start'], [symbol['func_def']]))
    parser.add_rule("statements_cont",ParserSyntax(symbol["Statements"], [symbol['Statements'], symbol['Statement']]))
    parser.add_rule("statements_end", ParserSyntax(symbol["Statements"], [symbol['Statement']]))

    parser.add_rule("return_form", ParserSyntax(symbol['Statement'], [tokens_list['return'], symbol['Expression'],
                                                                      tokens_list['semicolon']]))

    parser.add_rule("print_form", ParserSyntax(symbol['Statement'], [tokens_list['print'],
                                                                     symbol['Expression'],
                                                                     tokens_list['semicolon']]))

    parser.add_rule("real_declaration", ParserSyntax(symbol['Statement'], [symbol['declare_expression'],
                                                                           tokens_list['semicolon']]))

    parser.add_rule("declare_type_base", ParserSyntax(symbol['declare_type'], [tokens_list["type"]]))
    parser.add_rule("declare_separator_base", ParserSyntax(symbol["declare_separator"], [tokens_list["comma"]]))
    parser.add_rule("base_declare", ParserSyntax(symbol["declare_expression"], [symbol["declare_type"], token("string")]))

    # assignment rule
    parser.add_rule("assign_declare", ParserSyntax(symbol["declare_expression"],
                                                   [symbol["declare_expression"], tokens_list["equal"],
                                                    symbol["Expression"]]))

    parser.add_rule("integer_exp", ParserSyntax(symbol["Expression"], [token("integer")]))

    # bracket expressions
    parser.add_rule("paren_exp", ParserSyntax(symbol["Expression"],
                                              [tokens_list["open_paren"], symbol["Expression"],
                                               tokens_list["close_paren"]]))

    # arithmetic expressions
    parser.add_rule("add_exp", ParserSyntax(symbol["Expression"],
                                            [symbol["Expression"], tokens_list["add"], symbol["Expression"]]))
    parser.add_rule("minus_exp", ParserSyntax(symbol["Expression"],
                                            [symbol["Expression"], tokens_list["minus"], symbol["Expression"]]))
    parser.add_rule("mult_exp", ParserSyntax(symbol["Expression"],
                                             [symbol["Expression"], tokens_list["mult"], symbol["Expression"]]))
    parser.add_rule("div_exp", ParserSyntax(symbol["Expression"],
                                            [symbol["Expression"], tokens_list["div"], symbol["Expression"]]))

    parser.add_rule("mod_exp", ParserSyntax(symbol["Expression"],
                                            [symbol["Expression"], tokens_list["mod"], symbol["Expression"]]))

    # boolean expressions
    parser.add_rule("boolean_and_exp", ParserSyntax(symbol["Expression"],
                                                    [symbol["Expression"], tokens_list["booland"],
                                                     symbol["Expression"]]))

    parser.add_rule("boolean_or_exp", ParserSyntax(symbol["Expression"],
                                                   [symbol["Expression"], tokens_list["boolor"], symbol["Expression"]]))

    # comparisons expressions
    parser.add_rule("equal_to_exp", ParserSyntax(symbol["Expression"],
                                                 [symbol["Expression"], tokens_list["equalto"], symbol["Expression"]]))

    parser.add_rule("notequal_to_exp", ParserSyntax(symbol["Expression"],
                                                    [symbol["Expression"], tokens_list["notequalto"],
                                                     symbol["Expression"]]))

    parser.add_rule("lessequal_to_exp", ParserSyntax(symbol["Expression"],
                                                     [symbol["Expression"], tokens_list["lessequalto"],
                                                      symbol["Expression"]]))

    parser.add_rule("greaterequal_to_exp", ParserSyntax(symbol["Expression"],
                                                     [symbol["Expression"], tokens_list["greaterequalto"],
                                                      symbol["Expression"]]))

    parser.add_rule("lessthan_exp", ParserSyntax(symbol["Expression"],
                                                     [symbol["Expression"], tokens_list["lessthan"],
                                                      symbol["Expression"]]))

    parser.add_rule("greaterthan_exp", ParserSyntax(symbol["Expression"],
                                                     [symbol["Expression"], tokens_list["greaterthan"],
                                                      symbol["Expression"]]))

    parser.add_rule("E_func_call_start", ParserSyntax(symbol["func_call_start"], [symbol["Expression"],
                                                                                  tokens_list["open_paren"],
                                                                                  symbol["Expression"]]))
    parser.add_rule("E_func_call_end", ParserSyntax(symbol["Expression"], [symbol["func_call_start"],
                                                                           tokens_list["close_paren"]]))

    parser.add_rule("var_exp", ParserSyntax(symbol["Expression"], [token("string")]))

    parser.add_rule("exp_form", ParserSyntax(symbol["Statement"], [symbol["Expression"], tokens_list["semicolon"]]))

    # if else
    parser.add_rule("if_start_form", ParserSyntax(symbol["if"], [tokens_list["if"], tokens_list["open_paren"]]))

    parser.add_rule("if_exp", ParserSyntax(symbol["if"],  [symbol["if"], symbol["Expression"],
                                                           tokens_list["close_paren"],
                                                           tokens_list["curly_bracket"],
                                                           symbol["Statements"],
                                                           tokens_list["curly_close_bracket"]]))

    parser.add_rule("else_exp", ParserSyntax(symbol["else"],   [tokens_list["else"],
                                                                tokens_list["curly_bracket"],
                                                                symbol['Statements'],
                                                                tokens_list["curly_close_bracket"]]))

    parser.add_rule("if_else_exp", ParserSyntax(symbol["Statement"], [symbol["if"], symbol["else"]]))

    # while do

    parser.add_rule("while_expression", ParserSyntax(symbol['Statement'],  [symbol['while'],
                                                                            symbol['Expression'],
                                                                            tokens_list['close_paren'],
                                                                            tokens_list['curly_bracket'],
                                                                            symbol['Statements'],
                                                                            tokens_list['curly_close_bracket']]))

    parser.add_rule("semicolon_exp", ParserSyntax(symbol['Statement'], [tokens_list['semicolon']]))

    parser.add_rule("noarg_func_def_form", ParserSyntax(symbol['func_def'], [symbol['declare_expression'],
                                                                             tokens_list['open_paren'],
                                                                             tokens_list['close_paren'],
                                                                             tokens_list['curly_bracket'],
                                                                             symbol['Statements'],
                                                                             tokens_list['curly_close_bracket']]))

    parser.add_rule("noarg_func_dec_form", ParserSyntax(symbol['func_dec'], [symbol['declare_expression'],
                                                                             tokens_list['open_paren'],
                                                                             tokens_list['close_paren'],
                                                                             tokens_list['semicolon']]))

    parser.add_rule("for_exp", ParserSyntax(symbol['Statement'], [symbol['for_expr'], tokens_list['open_paren'],
                                                                  symbol['Statements'], symbol['Expression'],
                                                                  tokens_list['close_paren'],
                                                                  tokens_list['curly_bracket'], symbol['Statements'],
                                                                  tokens_list['curly_close_bracket']]))

    # assignment
    parser.add_rule("assign_exp", ParserSyntax(symbol["Expression"],
                                               [symbol["Expression"], tokens_list["equal"], symbol["Expression"]]))

    return parser.get_rule_dict()
