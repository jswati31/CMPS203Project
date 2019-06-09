# Main code

# Usage: python3 main.py --input_file test_file.c

from lexer_parser import Lexer, Parser
from assembly_code_gen import *
import sys, subprocess, argparse
from rules import *
import os


class SubsetC(object):
    def __init__(self):
        self.lexer = Lexer()
        self.parser = Parser()
        self.token_list = token_list_generator()
        self.sym_list = symbol_generator()
        self.parsing_syntax = None

    def lexing(self, input):
        program = input.read()
        splitprog = self.lexer.tokenize(program, self.token_list)
        return list(map(self.lexer.token_mapping, splitprog))

    def parsing(self, input):
        self.parsing_syntax = parser_gen(self.sym_list, self.token_list)
        return self.parser.build_ast(input, self.parsing_syntax, self.sym_list)

    def assembly_gen(self, input, filename):
        state = State()
        label_manager = LabelASM()
        state, complete_code = to_asm(input, state, label_manager, [], self.parsing_syntax)
        g = open(filename + ".s", "w")
        g.write(complete_code)
        g.close()


if __name__ == "__main__":

    parser = argparse.ArgumentParser(description='Subset of C compiler in Python.')
    parser.add_argument('--input_file', type=argparse.FileType('r'), help="the input c file")
    args = parser.parse_args()

    code = SubsetC()

    try:
        lexer_output = code.lexing(args.input_file)
    except:
        print("Could not read input file.")
        sys.exit(1)

    try:
        parser_output = code.parsing(lexer_output)
    except:
        print("Error parsing input.")
        sys.exit(1)

    try:
        output_name = args.input_file.name.split(".")[0]
        code.assembly_gen(parser_output, output_name)
    except:
        print("Error generating code.")
        sys.exit(1)
    else:
        print("Code Compiled Successfully")
        subprocess.call(["nasm", "-f", "macho64", output_name + ".s"])
        subprocess.call(["ld", "-e", "start", "-macosx_version_min", "10.13.0", "-static", output_name + ".o", "-o",
                         output_name])
        # execute the command
        cmd = "./" + output_name
        os.system(cmd)
        # remove assembly file
        cmd = 'rm ' + output_name + '.s'
        os.system(cmd)
        print("Done.")

