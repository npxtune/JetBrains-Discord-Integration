/*
 * Copyright 2017-2020 Aljoscha Grebe
 * Copyright 2023-2024 Axel JOLY (Azn9) <contact@azn9.dev>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Generated from Template.g4 by ANTLR 4.13.1
package dev.azn9.plugins.discord.render.templates.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class TemplateParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DOLLAR_SIGN=1, PERCENT_SIGN=2, PR_OPEN=3, PR_CLOSED=4, BR_OPEN=5, BR_CLOSED=6, 
		RAW_TEXT=7, IF_sym=8, NAME=9, TEXT=10;
	public static final int
		RULE_template = 0, RULE_raw_text_rule = 1, RULE_text_eval = 2, RULE_var = 3, 
		RULE_fun = 4, RULE_if_rule = 5;
	private static String[] makeRuleNames() {
		return new String[] {
			"template", "raw_text_rule", "text_eval", "var", "fun", "if_rule"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'$'", "'%'", "'('", "')'", "'{'", "'}'", null, "'if'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "DOLLAR_SIGN", "PERCENT_SIGN", "PR_OPEN", "PR_CLOSED", "BR_OPEN", 
			"BR_CLOSED", "RAW_TEXT", "IF_sym", "NAME", "TEXT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
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

	@Override
	public String getGrammarFileName() { return "Template.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TemplateParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateContext extends ParserRuleContext {
		public Text_evalContext text_eval() {
			return getRuleContext(Text_evalContext.class,0);
		}
		public TemplateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_template; }
	}

	public final TemplateContext template() throws RecognitionException {
		TemplateContext _localctx = new TemplateContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_template);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			text_eval();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Raw_text_ruleContext extends ParserRuleContext {
		public TerminalNode RAW_TEXT() { return getToken(TemplateParser.RAW_TEXT, 0); }
		public Raw_text_ruleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_raw_text_rule; }
	}

	public final Raw_text_ruleContext raw_text_rule() throws RecognitionException {
		Raw_text_ruleContext _localctx = new Raw_text_ruleContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_raw_text_rule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			match(RAW_TEXT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Text_evalContext extends ParserRuleContext {
		public List<TerminalNode> NAME() { return getTokens(TemplateParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(TemplateParser.NAME, i);
		}
		public List<TerminalNode> TEXT() { return getTokens(TemplateParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(TemplateParser.TEXT, i);
		}
		public List<Raw_text_ruleContext> raw_text_rule() {
			return getRuleContexts(Raw_text_ruleContext.class);
		}
		public Raw_text_ruleContext raw_text_rule(int i) {
			return getRuleContext(Raw_text_ruleContext.class,i);
		}
		public List<FunContext> fun() {
			return getRuleContexts(FunContext.class);
		}
		public FunContext fun(int i) {
			return getRuleContext(FunContext.class,i);
		}
		public List<VarContext> var() {
			return getRuleContexts(VarContext.class);
		}
		public VarContext var(int i) {
			return getRuleContext(VarContext.class,i);
		}
		public List<If_ruleContext> if_rule() {
			return getRuleContexts(If_ruleContext.class);
		}
		public If_ruleContext if_rule(int i) {
			return getRuleContext(If_ruleContext.class,i);
		}
		public List<TerminalNode> PR_OPEN() { return getTokens(TemplateParser.PR_OPEN); }
		public TerminalNode PR_OPEN(int i) {
			return getToken(TemplateParser.PR_OPEN, i);
		}
		public List<TerminalNode> PR_CLOSED() { return getTokens(TemplateParser.PR_CLOSED); }
		public TerminalNode PR_CLOSED(int i) {
			return getToken(TemplateParser.PR_CLOSED, i);
		}
		public List<TerminalNode> IF_sym() { return getTokens(TemplateParser.IF_sym); }
		public TerminalNode IF_sym(int i) {
			return getToken(TemplateParser.IF_sym, i);
		}
		public List<TerminalNode> PERCENT_SIGN() { return getTokens(TemplateParser.PERCENT_SIGN); }
		public TerminalNode PERCENT_SIGN(int i) {
			return getToken(TemplateParser.PERCENT_SIGN, i);
		}
		public Text_evalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_text_eval; }
	}

	public final Text_evalContext text_eval() throws RecognitionException {
		Text_evalContext _localctx = new Text_evalContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_text_eval);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(26);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
					case 1:
						{
						setState(16);
						match(NAME);
						}
						break;
					case 2:
						{
						setState(17);
						match(TEXT);
						}
						break;
					case 3:
						{
						setState(18);
						raw_text_rule();
						}
						break;
					case 4:
						{
						setState(19);
						fun();
						}
						break;
					case 5:
						{
						setState(20);
						var();
						}
						break;
					case 6:
						{
						setState(21);
						if_rule();
						}
						break;
					case 7:
						{
						setState(22);
						match(PR_OPEN);
						}
						break;
					case 8:
						{
						setState(23);
						match(PR_CLOSED);
						}
						break;
					case 9:
						{
						setState(24);
						match(IF_sym);
						}
						break;
					case 10:
						{
						setState(25);
						match(PERCENT_SIGN);
						}
						break;
					}
					} 
				}
				setState(30);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarContext extends ParserRuleContext {
		public TerminalNode DOLLAR_SIGN() { return getToken(TemplateParser.DOLLAR_SIGN, 0); }
		public TerminalNode NAME() { return getToken(TemplateParser.NAME, 0); }
		public TerminalNode BR_OPEN() { return getToken(TemplateParser.BR_OPEN, 0); }
		public TerminalNode BR_CLOSED() { return getToken(TemplateParser.BR_CLOSED, 0); }
		public VarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var; }
	}

	public final VarContext var() throws RecognitionException {
		VarContext _localctx = new VarContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_var);
		try {
			setState(37);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(31);
				match(DOLLAR_SIGN);
				setState(32);
				match(NAME);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(33);
				match(DOLLAR_SIGN);
				setState(34);
				match(BR_OPEN);
				setState(35);
				match(NAME);
				setState(36);
				match(BR_CLOSED);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunContext extends ParserRuleContext {
		public TerminalNode DOLLAR_SIGN() { return getToken(TemplateParser.DOLLAR_SIGN, 0); }
		public TerminalNode NAME() { return getToken(TemplateParser.NAME, 0); }
		public List<TerminalNode> BR_OPEN() { return getTokens(TemplateParser.BR_OPEN); }
		public TerminalNode BR_OPEN(int i) {
			return getToken(TemplateParser.BR_OPEN, i);
		}
		public List<Text_evalContext> text_eval() {
			return getRuleContexts(Text_evalContext.class);
		}
		public Text_evalContext text_eval(int i) {
			return getRuleContext(Text_evalContext.class,i);
		}
		public List<TerminalNode> BR_CLOSED() { return getTokens(TemplateParser.BR_CLOSED); }
		public TerminalNode BR_CLOSED(int i) {
			return getToken(TemplateParser.BR_CLOSED, i);
		}
		public FunContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fun; }
	}

	public final FunContext fun() throws RecognitionException {
		FunContext _localctx = new FunContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_fun);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			match(DOLLAR_SIGN);
			setState(40);
			match(NAME);
			setState(45); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(41);
				match(BR_OPEN);
				setState(42);
				text_eval();
				setState(43);
				match(BR_CLOSED);
				}
				}
				setState(47); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==BR_OPEN );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class If_ruleContext extends ParserRuleContext {
		public TerminalNode PERCENT_SIGN() { return getToken(TemplateParser.PERCENT_SIGN, 0); }
		public TerminalNode IF_sym() { return getToken(TemplateParser.IF_sym, 0); }
		public TerminalNode PR_OPEN() { return getToken(TemplateParser.PR_OPEN, 0); }
		public List<Text_evalContext> text_eval() {
			return getRuleContexts(Text_evalContext.class);
		}
		public Text_evalContext text_eval(int i) {
			return getRuleContext(Text_evalContext.class,i);
		}
		public TerminalNode PR_CLOSED() { return getToken(TemplateParser.PR_CLOSED, 0); }
		public List<TerminalNode> BR_OPEN() { return getTokens(TemplateParser.BR_OPEN); }
		public TerminalNode BR_OPEN(int i) {
			return getToken(TemplateParser.BR_OPEN, i);
		}
		public List<TerminalNode> BR_CLOSED() { return getTokens(TemplateParser.BR_CLOSED); }
		public TerminalNode BR_CLOSED(int i) {
			return getToken(TemplateParser.BR_CLOSED, i);
		}
		public If_ruleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_rule; }
	}

	public final If_ruleContext if_rule() throws RecognitionException {
		If_ruleContext _localctx = new If_ruleContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_if_rule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			match(PERCENT_SIGN);
			setState(50);
			match(IF_sym);
			setState(51);
			match(PR_OPEN);
			setState(52);
			text_eval();
			setState(53);
			match(PR_CLOSED);
			setState(54);
			match(BR_OPEN);
			setState(55);
			text_eval();
			setState(56);
			match(BR_CLOSED);
			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==BR_OPEN) {
				{
				setState(57);
				match(BR_OPEN);
				setState(58);
				text_eval();
				setState(59);
				match(BR_CLOSED);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\n@\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u001b\b\u0002\n"+
		"\u0002\f\u0002\u001e\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0003\u0003&\b\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0004\u0004.\b"+
		"\u0004\u000b\u0004\f\u0004/\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0003\u0005>\b\u0005\u0001\u0005\u0000"+
		"\u0000\u0006\u0000\u0002\u0004\u0006\b\n\u0000\u0000F\u0000\f\u0001\u0000"+
		"\u0000\u0000\u0002\u000e\u0001\u0000\u0000\u0000\u0004\u001c\u0001\u0000"+
		"\u0000\u0000\u0006%\u0001\u0000\u0000\u0000\b\'\u0001\u0000\u0000\u0000"+
		"\n1\u0001\u0000\u0000\u0000\f\r\u0003\u0004\u0002\u0000\r\u0001\u0001"+
		"\u0000\u0000\u0000\u000e\u000f\u0005\u0007\u0000\u0000\u000f\u0003\u0001"+
		"\u0000\u0000\u0000\u0010\u001b\u0005\t\u0000\u0000\u0011\u001b\u0005\n"+
		"\u0000\u0000\u0012\u001b\u0003\u0002\u0001\u0000\u0013\u001b\u0003\b\u0004"+
		"\u0000\u0014\u001b\u0003\u0006\u0003\u0000\u0015\u001b\u0003\n\u0005\u0000"+
		"\u0016\u001b\u0005\u0003\u0000\u0000\u0017\u001b\u0005\u0004\u0000\u0000"+
		"\u0018\u001b\u0005\b\u0000\u0000\u0019\u001b\u0005\u0002\u0000\u0000\u001a"+
		"\u0010\u0001\u0000\u0000\u0000\u001a\u0011\u0001\u0000\u0000\u0000\u001a"+
		"\u0012\u0001\u0000\u0000\u0000\u001a\u0013\u0001\u0000\u0000\u0000\u001a"+
		"\u0014\u0001\u0000\u0000\u0000\u001a\u0015\u0001\u0000\u0000\u0000\u001a"+
		"\u0016\u0001\u0000\u0000\u0000\u001a\u0017\u0001\u0000\u0000\u0000\u001a"+
		"\u0018\u0001\u0000\u0000\u0000\u001a\u0019\u0001\u0000\u0000\u0000\u001b"+
		"\u001e\u0001\u0000\u0000\u0000\u001c\u001a\u0001\u0000\u0000\u0000\u001c"+
		"\u001d\u0001\u0000\u0000\u0000\u001d\u0005\u0001\u0000\u0000\u0000\u001e"+
		"\u001c\u0001\u0000\u0000\u0000\u001f \u0005\u0001\u0000\u0000 &\u0005"+
		"\t\u0000\u0000!\"\u0005\u0001\u0000\u0000\"#\u0005\u0005\u0000\u0000#"+
		"$\u0005\t\u0000\u0000$&\u0005\u0006\u0000\u0000%\u001f\u0001\u0000\u0000"+
		"\u0000%!\u0001\u0000\u0000\u0000&\u0007\u0001\u0000\u0000\u0000\'(\u0005"+
		"\u0001\u0000\u0000(-\u0005\t\u0000\u0000)*\u0005\u0005\u0000\u0000*+\u0003"+
		"\u0004\u0002\u0000+,\u0005\u0006\u0000\u0000,.\u0001\u0000\u0000\u0000"+
		"-)\u0001\u0000\u0000\u0000./\u0001\u0000\u0000\u0000/-\u0001\u0000\u0000"+
		"\u0000/0\u0001\u0000\u0000\u00000\t\u0001\u0000\u0000\u000012\u0005\u0002"+
		"\u0000\u000023\u0005\b\u0000\u000034\u0005\u0003\u0000\u000045\u0003\u0004"+
		"\u0002\u000056\u0005\u0004\u0000\u000067\u0005\u0005\u0000\u000078\u0003"+
		"\u0004\u0002\u00008=\u0005\u0006\u0000\u00009:\u0005\u0005\u0000\u0000"+
		":;\u0003\u0004\u0002\u0000;<\u0005\u0006\u0000\u0000<>\u0001\u0000\u0000"+
		"\u0000=9\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000>\u000b\u0001"+
		"\u0000\u0000\u0000\u0005\u001a\u001c%/=";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
