package compiler;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

public class CustomLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();

	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "WHILE", "RETURN", 
		"IF", "INT", "VOID", "PLUS", "MINUS", "MULTIPLY", "DIVIDE", "EXCLAMATION", 
		"GREATER", "LESS", "EQAUL", "GREATEROREQUAL", "LESSOREQUAL", "NOTEQUAL", 
		"IDENTIFIER", "NUMBER", "WS", "NEWLINE"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", "'('", "','", "')'", "'{'", "'}'", "'='", "'while'", "'return'", 
		"'if'", "'int'", "'void'", "'+'", "'-'", "'*'", "'/'", "'!'", "'>'", "'<'", 
		"'=='", "'>='", "'<='", "'!='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, "WHILE", "RETURN", "IF", 
		"INT", "VOID", "PLUS", "MINUS", "MULTIPLY", "DIVIDE", "EXCLAMATION", "GREATER", 
		"LESS", "EQAUL", "GREATEROREQUAL", "LESSOREQUAL", "NOTEQUAL", "IDENTIFIER", 
		"NUMBER", "WS", "NEWLINE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);



	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public CustomLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SubsetC.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\35\u0094\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3"+
		"\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\17"+
		"\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25"+
		"\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\7\31}\n\31\f\31"+
		"\16\31\u0080\13\31\3\32\6\32\u0083\n\32\r\32\16\32\u0084\3\33\6\33\u0088"+
		"\n\33\r\33\16\33\u0089\3\33\3\33\3\34\5\34\u008f\n\34\3\34\3\34\3\34\3"+
		"\34\2\2\35\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"+
		"\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67"+
		"\35\3\2\6\5\2C\\aac|\6\2\62;C\\aac|\3\2\62;\4\2\13\13\"\"\2\u0097\2\3"+
		"\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2"+
		"\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31"+
		"\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2"+
		"\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2"+
		"\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\39\3\2\2\2\5;\3\2\2"+
		"\2\7=\3\2\2\2\t?\3\2\2\2\13A\3\2\2\2\rC\3\2\2\2\17E\3\2\2\2\21G\3\2\2"+
		"\2\23M\3\2\2\2\25T\3\2\2\2\27W\3\2\2\2\31[\3\2\2\2\33`\3\2\2\2\35b\3\2"+
		"\2\2\37d\3\2\2\2!f\3\2\2\2#h\3\2\2\2%j\3\2\2\2\'l\3\2\2\2)n\3\2\2\2+q"+
		"\3\2\2\2-t\3\2\2\2/w\3\2\2\2\61z\3\2\2\2\63\u0082\3\2\2\2\65\u0087\3\2"+
		"\2\2\67\u008e\3\2\2\29:\7=\2\2:\4\3\2\2\2;<\7*\2\2<\6\3\2\2\2=>\7.\2\2"+
		">\b\3\2\2\2?@\7+\2\2@\n\3\2\2\2AB\7}\2\2B\f\3\2\2\2CD\7\177\2\2D\16\3"+
		"\2\2\2EF\7?\2\2F\20\3\2\2\2GH\7y\2\2HI\7j\2\2IJ\7k\2\2JK\7n\2\2KL\7g\2"+
		"\2L\22\3\2\2\2MN\7t\2\2NO\7g\2\2OP\7v\2\2PQ\7w\2\2QR\7t\2\2RS\7p\2\2S"+
		"\24\3\2\2\2TU\7k\2\2UV\7h\2\2V\26\3\2\2\2WX\7k\2\2XY\7p\2\2YZ\7v\2\2Z"+
		"\30\3\2\2\2[\\\7x\2\2\\]\7q\2\2]^\7k\2\2^_\7f\2\2_\32\3\2\2\2`a\7-\2\2"+
		"a\34\3\2\2\2bc\7/\2\2c\36\3\2\2\2de\7,\2\2e \3\2\2\2fg\7\61\2\2g\"\3\2"+
		"\2\2hi\7#\2\2i$\3\2\2\2jk\7@\2\2k&\3\2\2\2lm\7>\2\2m(\3\2\2\2no\7?\2\2"+
		"op\7?\2\2p*\3\2\2\2qr\7@\2\2rs\7?\2\2s,\3\2\2\2tu\7>\2\2uv\7?\2\2v.\3"+
		"\2\2\2wx\7#\2\2xy\7?\2\2y\60\3\2\2\2z~\t\2\2\2{}\t\3\2\2|{\3\2\2\2}\u0080"+
		"\3\2\2\2~|\3\2\2\2~\177\3\2\2\2\177\62\3\2\2\2\u0080~\3\2\2\2\u0081\u0083"+
		"\t\4\2\2\u0082\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0082\3\2\2\2\u0084"+
		"\u0085\3\2\2\2\u0085\64\3\2\2\2\u0086\u0088\t\5\2\2\u0087\u0086\3\2\2"+
		"\2\u0088\u0089\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008b"+
		"\3\2\2\2\u008b\u008c\b\33\2\2\u008c\66\3\2\2\2\u008d\u008f\7\17\2\2\u008e"+
		"\u008d\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0091\7\f"+
		"\2\2\u0091\u0092\3\2\2\2\u0092\u0093\b\34\2\2\u00938\3\2\2\2\7\2~\u0084"+
		"\u0089\u008e\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}