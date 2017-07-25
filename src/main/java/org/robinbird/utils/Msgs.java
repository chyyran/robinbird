package org.robinbird.utils;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by seokhyun on 5/26/17.
 */
public class Msgs {

	public enum Key {
		ROOT_SOURCE_PATH_NOT_PROVIDED,
		IOEXCEPTION_WHILE_READING_SOURCE_CODES,
		ALREADY_EXISTING_TYPE_NAME,
		CURRENT_CLASS_IS_NULL_WHILE_WALKING_THROUGH_PARSE_TREE,
		FAILED_TO_FIND_MEMBER_TYPE,
		PRESENTATION_OPTION_IS_NOT_VALID,
		NULL_POINTER_ENCOUNTERED,
		WRONG_REGEXP_FOR_TERMINAL,
		WRONG_REGEXP_FOR_EXCLUSION,
		REGEXP_FOR_TERMINAL_IS_NOT_GIVEN,
		REGEXP_FOR_EXCLUSION_IS_NOT_GIVEN,
		LIST_FOR_PACKAGE_NAME_IS_EMPTY
	}

	private static final Map<Key, String> msgMap;
	static  {
		msgMap = new ImmutableMap.Builder<Key, String>()
			.put(Key.ROOT_SOURCE_PATH_NOT_PROVIDED, "Root source path should be provided.")
			.put(Key.IOEXCEPTION_WHILE_READING_SOURCE_CODES, "(Unchecked)IOException is thrown while reading source codes.")
			.put(Key.ALREADY_EXISTING_TYPE_NAME, "Already existing type name is given: %s.")
			.put(Key.CURRENT_CLASS_IS_NULL_WHILE_WALKING_THROUGH_PARSE_TREE, "Current class is null while parsing class.")
			.put(Key.FAILED_TO_FIND_MEMBER_TYPE, "Failed to find member type for %s.")
			.put(Key.PRESENTATION_OPTION_IS_NOT_VALID, "Invalid presentation option: %s.")
			.put(Key.NULL_POINTER_ENCOUNTERED, "Null pointer encountered in %s.")
			.put(Key.WRONG_REGEXP_FOR_TERMINAL, "Wrong regular expression pattern is given for terminal: %s.")
			.put(Key.WRONG_REGEXP_FOR_EXCLUSION, "Wrong regular expression pattern is given for exclusion: %s.")
			.put(Key.REGEXP_FOR_TERMINAL_IS_NOT_GIVEN, "Regular expression pattern for a terminal is not given.")
			.put(Key.REGEXP_FOR_EXCLUSION_IS_NOT_GIVEN, "Regular expression pattern for exclusion is not given.")
			.put(Key.LIST_FOR_PACKAGE_NAME_IS_EMPTY, "List for defining package name is empty.")
			.build();
	}

	public static String get(Key k) {
		return msgMap.get(k);
	}

	public static String get(Key k, String msg) { return String.format(msgMap.get(k), msg); }

	public static String get(Key k, Exception e) {
		return msgMap.get(k) + getNewLineStackTrace(e);
	}

	public static String get(Key k, Exception e, String msg) {
		return get(k, msg) + getNewLineStackTrace(e);
	}

	private static String getNewLineStackTrace(Exception e) { return "\n" + Throwables.getStackTraceAsString(e); }


}
