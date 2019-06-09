# Assembly code generator


class LabelASM:
    def __init__(self):
        self.labelnum = 0

    def get_label(self):
        label_name = "__label" + str(self.labelnum)
        self.labelnum += 1
        return label_name


class Type:
    def __init__(self, type_name="int"):
        self.type_name = type_name

    def __repr__(self):
        return self.type_name

    def __eq__(self, other):
        return self.type_name == other.type_name


class State:
    def __init__(self, var_offset=0, symbols=[], funcs=[], t=Type()):
        self.var_offset = var_offset
        self.symbols = symbols[:]
        self.funcs = funcs[:]
        self.t = t

    def is_declared(self, name):
        return name in [dec[0] for dec in self.symbols]

    def func_declared(self, func_name):
        return func_name in [func["fname"] for func in self.funcs]

    def add_space(self):
        s = self.c()
        s.var_offset += 1
        return s

    def add(self, name, t):
        if self.is_declared(name):
            raise Exception
        else:
            s = self.c()
            s.var_offset += 1
            s.symbols += [(name, s.var_offset, t)]
            return s

    def get(self, name):
        var_loc = [var for var in self.symbols if var[0] == name]
        if var_loc:
            return (var_loc[0][1], var_loc[0][2])
        else:
            raise Exception

    def add_func(self, func_name, func_type, func_args, label):
        if self.func_declared(func_name):
            raise Exception
        else:
            s = self.c()
            s.funcs += [{"fname": func_name,
                         "ftype": func_type,
                         "args": func_args,
                         "label": label}]
            return s

    def get_func(self, func_name):
        func_loc = [func for func in self.funcs if func["fname"] == func_name]
        if func_loc:
            return func_loc[0]
        else:
            raise Exception

    def c(self):
        return State(self.var_offset, self.symbols, self.funcs, self.t)


def to_asm(root, state, label, asm_code_list, parser_syn, has_else=False, endelse_label="", loop_break=None,
           loop_continue=None):

    if root.rule == parser_syn['main_func_def_cont'] or root.rule == parser_syn['main_func_dec_cont']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn, loop_break=loop_break,
                       loop_continue=loop_continue)[0]
        state = to_asm(root.children[1], state, label, asm_code_list, parser_syn, loop_break=loop_break,
                       loop_continue=loop_continue)[0]

    elif root.rule == parser_syn['main_function_declaration'] or root.rule == parser_syn['main_function_definition']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn, loop_break=loop_break,
                       loop_continue=loop_continue)[0]

    elif root.rule == parser_syn['noarg_func_dec_form']:
        state = state.add_func(root.children[0].children[1].text, Type("int"), [], label.get_label())

    elif root.rule == parser_syn['noarg_func_def_form']:
        if not state.func_declared(root.children[0].children[1].char):
            state = state.add_func(root.children[0].children[1].char, Type("int"), [], label.get_label())

        f = state.get_func(root.children[0].children[1].char)
        asm_code_list += [f["label"] + ":"]

        info_t = state.c()
        to_asm(root.children[4], info_t, label, asm_code_list, parser_syn)

    elif root.rule == parser_syn['statements_cont']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn, loop_break=loop_break,
                       loop_continue=loop_continue)[0]
        asm_code_list += ["\t" + "lea" + " " + "rsp" + "," + "[rbp - " + str(state.var_offset * 8) + "]"]

        state = to_asm(root.children[1], state, label, asm_code_list, parser_syn, loop_break=loop_break,
                       loop_continue=loop_continue)[0]

    elif root.rule == parser_syn['statements_end']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn, loop_break=loop_break,
                       loop_continue=loop_continue)[0]
        
    elif root.rule == parser_syn['return_form']:
        state = to_asm(root.children[1], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "mov" + " " + "rax" + "," + "[rbp + 8]"]
        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "mov" + " " + "[rbp + 8]" + "," + "rbx"]
        asm_code_list += ["\t" + "lea" + " " + "rsp" + "," + "[rbp + 8]"]
        asm_code_list += ["\t" + "mov" + " " + "rbp" + "," + "[rbp]"]
        asm_code_list += ["\t" + "jmp" + " " + "rax"]

    elif root.rule == parser_syn['real_declaration']:
        node = root
        while node.rule != parser_syn['declare_type_base']:
            node = node.children[0]
        dec_type = node.children[0].char
        if dec_type != "int":
            raise Exception
        else:
            node = root
            next_val = None
            next_array = False
            next_size = 0

            declarations = []

            while True:
                if node.rule == parser_syn['real_declaration']:
                    node = node.children[0]
                elif node.rule == parser_syn['assign_declare']:
                    next_val = node.children[2]
                    node = node.children[0]
                elif node.rule == parser_syn['base_declare']:
                    declarations.append((node.children[1].char, next_val, next_array, next_size))
                    break

            declarations.reverse()

            for (name, node, is_array, array_len) in declarations:
                if not is_array:
                    if node:
                        state = to_asm(node, state, label, asm_code_list, parser_syn)[0]
                    else:
                        asm_code_list += ["\t" + "push" + " " + "0"]
                    state = state.add(name, Type("int"))

                asm_code_list += ["\t" + "lea" + " " + "rsp" + "," + "[rbp - " + str(state.var_offset * 8) + "]"]

    elif root.rule == parser_syn['integer_exp']:
        asm_code_list += ["\t" + "push" + " " + root.children[0].char]
        state.t = Type()
        
    elif root.rule == parser_syn['paren_exp']:
        state = to_asm(root.children[1], state, label, asm_code_list, parser_syn)[0]
        
    elif root.rule == parser_syn['add_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]

        if root.children[1].char == "+":
            asm_code_list += ["\t" + "add" + " " + "rax" + "," + "rbx"]
        else:
            raise Exception

        asm_code_list += ["\t" + "push" + " " + "rax"]
        state.t = Type("int")

    elif root.rule == parser_syn['minus_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]

        if root.children[1].char == "-":
            asm_code_list += ["\t" + "sub" + " " + "rax" + "," + "rbx"]
        else:
            raise Exception

        asm_code_list += ["\t" + "push" + " " + "rax"]
        state.t = Type("int")
        
    elif root.rule == parser_syn['mult_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]

        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "imul" + " " + "rax" + "," + "rbx"]
        asm_code_list += ["\t" + "push" + " " + "rax"]
        
    elif root.rule == parser_syn['div_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]

        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "cqo"]
        asm_code_list += ["\t" + "idiv" + " " + "rbx"]
        asm_code_list += ["\t" + "push" + " " + "rax"]
        
    elif root.rule == parser_syn['mod_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]

        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "mov" + " " + "rdx" + "," + "0"]
        asm_code_list += ["\t" + "cqo"]
        asm_code_list += ["\t" + "imul" + " " + "rbx"]
        asm_code_list += ["\t" + "push" + " " + "rdx"]

    elif root.rule == parser_syn['boolean_and_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        push_0 = label.get_label()
        end = label.get_label()
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "0"]
        asm_code_list += ["\t" + "je" + " " + push_0]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "0"]
        asm_code_list += ["\t" + "je" + " " + push_0]
        asm_code_list += ["\t" + "push" + " " + "1"]
        asm_code_list += ["\t" + "jmp" + " " + end]
        asm_code_list += [push_0 + ":"]
        asm_code_list += ["\t" + "push" + " " + "0"]
        asm_code_list += [end + ":"]

        state.t = Type("int")

    elif root.rule == parser_syn['boolean_or_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        push_1 = label.get_label()
        end = label.get_label()
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "0"]
        asm_code_list += ["\t" + "jne" + " " + push_1]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "0"]
        asm_code_list += ["\t" + "jne" + " " + push_1]
        asm_code_list += ["\t" + "push" + " " + "0"]
        asm_code_list += ["\t" + "jmp" + " " + end]
        asm_code_list += [push_1 + ":"]
        asm_code_list += ["\t" + "push" + " " + "1"]
        asm_code_list += [end + ":"]
        state.t = Type("int")
        
    elif root.rule == parser_syn['equal_to_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "mov" + " " + "rcx" + "," + "0"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "rbx"]
        if root.children[1].char == "==":
            asm_code_list += ["\t" + "sete" + " " + "cl"]
        else:
            raise Exception
        asm_code_list += ["\t" + "push" + " " + "rcx"]

        state.t = Type("int")

    elif root.rule == parser_syn['notequal_to_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "mov" + " " + "rcx" + "," + "0"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "rbx"]
        if root.children[1].char == "!=":
            asm_code_list += ["\t" + "setne" + " " + "cl"]
        else:
            raise Exception

        asm_code_list += ["\t" + "push" + " " + "rcx"]

        state.t = Type("int")

    elif root.rule == parser_syn['lessthan_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "mov" + " " + "rcx" + "," + "0"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "rbx"]
        if root.children[1].char == "<":
            asm_code_list += ["\t" + "setl" + " " + "cl"]
        else:
            raise Exception
        asm_code_list += ["\t" + "push" + " " + "rcx"]

        state.t = Type("int")

    elif root.rule == parser_syn['lessequal_to_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "mov" + " " + "rcx" + "," + "0"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "rbx"]
        if root.children[1].char == "<=":
            asm_code_list += ["\t" + "setle" + " " + "cl"]
        else:
            raise Exception
        asm_code_list += ["\t" + "push" + " " + "rcx"]

        state.t = Type("int")

    elif root.rule == parser_syn['greaterthan_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "mov" + " " + "rcx" + "," + "0"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "rbx"]
        if root.children[1].char == ">":
            asm_code_list += ["\t" + "setg" + " " + "cl"]
        else:
            raise Exception
        asm_code_list += ["\t" + "push" + " " + "rcx"]

        state.t = Type("int")

    elif root.rule == parser_syn['greaterequal_to_exp']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rbx"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "mov" + " " + "rcx" + "," + "0"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "rbx"]
        if root.children[1].char == ">=":
            asm_code_list += ["\t" + "setge" + " " + "cl"]
        else:
            raise Exception
        asm_code_list += ["\t" + "push" + " " + "rcx"]

        state.t = Type("int")

    elif root.rule == parser_syn['assign_exp']:
        left_base = root.children[0]
        
        if left_base.rule == parser_syn['var_exp']:
            var_loc = state.get(left_base.children[0].char)
            state = to_asm(root.children[2], state, label, asm_code_list, parser_syn)[0]
            asm_code_list += ["\t" + "pop" + " " + "rax"]
            asm_code_list += ["\t" + "mov" + " " + "[rbp - " + str(8*var_loc[0]) + "]", + "," + "rax"]
            asm_code_list += ["\t" + "push" + " " + "rax"]
            state.t = var_loc[1]
        else:
            raise Exception

    elif root.rule == parser_syn['E_func_call_end']:
        Es = []
        node = root
        while True:
            if node.rule == parser_syn['E_func_call_end']:
                node = node.children[0]
            elif node.rule == parser_syn['E_func_call_start']:
                Es.append(node.children[2])
                func = node.children[0]
                Es.reverse()
                break
            
        if func.rule != parser_syn['var_exp']:
            raise Exception
        else:
            fname = func.children[0].char
            f = state.get_func(fname)
            if len(f["args"]) != len(Es):
                raise Exception
            
            retlabel = label.get_label()
            asm_code_list += ["\t" + "lea" + " " + "rax" + "," + "[rel " + retlabel + "]"]
            asm_code_list += ["\t" + "push" + " " + "rax"]
            asm_code_list += ["\t" + "push" + " " + "rbp"]
            for node in Es:
                state = to_asm(node, state, label, asm_code_list, parser_syn)[0]
            asm_code_list += ["\t" + "lea" + " " + "rbp" + "," + "[rsp + " + str(8*len(Es)) + "]"]
            asm_code_list += ["\t" + "jmp" + " " + f["label"]]
            asm_code_list += [retlabel + ":"]
            state.t = f["ftype"]

    elif root.rule == parser_syn['var_exp']:
        var_loc = state.get(root.children[0].char)
        asm_code_list += ["\t" + "push" + " " + "qword [rbp - " + str(8*var_loc[0]) + "]"]
        state.t = var_loc[1]

    elif root.rule == parser_syn['exp_form']:
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn)[0]

    elif root.rule == parser_syn['if_exp']:
        state = to_asm(root.children[1], state, label, asm_code_list, parser_syn, loop_break=loop_break, loop_continue=loop_continue)[0]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "0"]
        endif_label = label.get_label()
        asm_code_list += ["\t" + "je" + " " + endif_label]

        to_asm(root.children[4], state, label, asm_code_list, parser_syn, loop_break=loop_break, loop_continue=loop_continue)

        if has_else:
            asm_code_list += ["\t" + "jmp" + " " + endelse_label]

        asm_code_list += [endif_label + ":"]

    elif root.rule == parser_syn['else_exp']:
        to_asm(root.children[2], state, label, asm_code_list, parser_syn, loop_break=loop_break, loop_continue=loop_continue)

    elif root.rule == parser_syn['if_else_exp']:
        end_else = label.get_label()
        state = to_asm(root.children[0], state, label, asm_code_list, parser_syn, True, end_else, loop_break=loop_break,
                       loop_continue=loop_continue)[0]
        state = to_asm(root.children[1], state, label, asm_code_list, parser_syn, loop_break=loop_break,
                       loop_continue=loop_continue)[0]
        asm_code_list += [end_else + ":"]

    elif root.rule == parser_syn['while_expression']:
        startwhile = label.get_label()
        endwhile = label.get_label()

        asm_code_list += [startwhile + ":"]
        state = to_asm(root.children[1], state, label, asm_code_list, parser_syn)[0]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "0"]
        asm_code_list += ["\t" + "je" + " " + endwhile]
        to_asm(root.children[4], state, label, asm_code_list, parser_syn, loop_break=endwhile, loop_continue=startwhile)
        asm_code_list += ["\t" + "lea" + " " + "rsp" + "," + "[rbp - " + str(state.var_offset * 8) +  "]"]
        asm_code_list += ["\t" + "jmp" + " " + startwhile]
        asm_code_list += [endwhile + ":"]

    elif root.rule == parser_syn['for_exp']:
        startfor = label.get_label()
        endfor = label.get_label()
        cont_point = label.get_label()

        info_temp = to_asm(root.children[0].children[0].children[0].children[1], state, label, asm_code_list,
                           parser_syn)[0]
        asm_code_list += [startfor + ":"]
        exp2 = root.children[0].children[0].children[1].children[0]
        if exp2.rule == parser_syn['exp_form']:
            to_asm(exp2, info_temp, label, asm_code_list, parser_syn)
            asm_code_list += ["\t" + "pop" + " " + "rax"]
            asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "0"]
            asm_code_list += ["\t" + "je" + " " + endfor]
            to_asm(root.children[2], info_temp, label, asm_code_list, parser_syn, loop_break=endfor, loop_continue=cont_point)

            asm_code_list += [cont_point + ":"]
            
            to_asm(root.children[0].children[1], info_temp, label, asm_code_list, parser_syn)

            asm_code_list += ["\t" + "lea" + " " + "rsp" + "," + "[rbp - " + str(info_temp.var_offset * 8) +  "]"]
            asm_code_list += ["\t" + "jmp" + " " + startfor]
            asm_code_list += [endfor + ":"]
        else:
            raise Exception

    elif root.rule == parser_syn['print_form']:
        state = to_asm(root.children[1], state, label, asm_code_list, parser_syn)[0]
        isneg = label.get_label()
        nextchar = label.get_label()
        done = label.get_label()
        nodash = label.get_label()
        asm_code_list += ["\t" + "mov" + " " + "r10" + "," + "10"]
        asm_code_list += ["\t" + "pop" + " " + "rax"]
        asm_code_list += ["\t" + "mov" + " " + "r8" + "," + "1"]
        asm_code_list += ["\t" + "mov" + " " + "rbx" + "," + "10"]
        asm_code_list += ["\t" + "mov" + " " + "r9" + "," + "0"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "0"]
        asm_code_list += ["\t" + "jl" + " " + isneg]
        asm_code_list += ["\t" + "jmp" + " " + nextchar]
        asm_code_list += [isneg + ":"]
        asm_code_list += ["\t" + "mov" + " " + "r9" + "," + "1"]
        asm_code_list += ["\t" + "neg" + " " + "rax"]
        asm_code_list += [nextchar + ":"]
        asm_code_list += ["\t" + "shl" + " " + "rbx" + "," + "8"]
        asm_code_list += ["\t" + "mov" + " " "rdx" + "," + "0"]
        asm_code_list += ["\t" + "cqo"]
        asm_code_list += ["\t" + "idiv" + " " + "r10"]
        asm_code_list += ["\t" + "add" + " " + "rbx" + "," + "rdx"]
        asm_code_list += ["\t" + "add" + " " + "rbx" + "," + "48"]
        asm_code_list += ["\t" + "inc" + " " + "r8"]
        asm_code_list += ["\t" + "cmp" + " " + "rax" + "," + "0"]
        asm_code_list += ["\t" + "je" + " " + done]
        asm_code_list += ["\t" + "jmp" + " " + nextchar]
        asm_code_list += [done + ":"]
        asm_code_list += ["\t" + "cmp" + " " + "r9" + "," + "0"]
        asm_code_list += ["\t" + "je" + " " + nodash]
        asm_code_list += ["\t" + "shl" + " " + "rbx" + "," + "8"]
        asm_code_list += ["\t" + "add" + " " + "rbx" + "," + "45"]
        asm_code_list += ["\t" + "inc" + " " + "r8"]
        asm_code_list += [nodash + ":"]
        asm_code_list += ["\t" + "mov" + " " + "rdx" + "," + "r8"]
        asm_code_list += ["\t" + "push" + " " + "rbx"]
        asm_code_list += ["\t" + "mov" + " " + "rax" + ","  + "0x2000004"]
        asm_code_list += ["\t" + "mov" + " " + "rdi" + "," + "1"]
        asm_code_list += ["\t" + "mov" + " " + "rsi" + "," + "rsp"]
        asm_code_list += ["\t" + "syscall"]

    elif root.rule == parser_syn['semicolon_exp']:
        pass

    else:
        raise Exception

    setup =      ["\tglobal start",
                  "",
                  "\tsection .text",
                  "",
                  "start:",
                  "\tmov rbp, rsp",
                  "\tlea rax, [rel retmain]",
                  "\tpush rax",
                  "\tpush rbp",
                  "\tmov rbp, rsp",
                  "\tjmp " + "__label0",
                  "retmain:",
                  "\tpop rdi",
                  "\tmov rax, 0x2000001",
                  "\tsyscall"]
    data = ["", "\tsection .data", "var:\tdb 0"]
    asm_code = '\n'.join(setup + asm_code_list + data)
    
    return state, asm_code
