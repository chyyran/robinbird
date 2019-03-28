package org.robinbird.main.newpresentation;

import java.util.Map;
import org.robinbird.main.newmodel.AnalysisContext;

/**
 * Created by seokhyun on 6/7/17.
 */
public class SimplePresentation implements AnalysisContextPresentation {

	public String present(AnalysisContext analysisContext) {

		/*
		StringAppender sa = new StringAppender();
		sa.appendLine("//----------------------------------------------------");
		sa.appendLine("// Packages");
		for (Package classPackage : analysisContext.getPackages()) {
			sa.appendLine(classPackage.getName());
		}

		sa.appendLine("//----------------------------------------------------");
		sa.appendLine("// Classes");
		for (Class classObj : analysisContext.getClasses()) {
			Package classPackage = classObj.getClassPackage();
			if (classPackage != null) {
				sa.append("[" + classPackage.getName() + "] ");
			}
			sa.appendLine(classObj.getName());
			for (Map.Entry<String, Member> entry : classObj.getMemberVariables().entrySet()) {
				sa.appendLine(String.format("\t%s : %s", entry.getKey(), entry.getValue().getType().getName()));
			}
			for (Map.Entry<String, MemberFunction> entry :  classObj.getMemberFunctions().entrySet()) {
				sa.appendLine(String.format("\t%s : %s", entry.getKey(), entry.getValue().getType().getName()));
			}
		}
		sa.appendLine("//----------------------------------------------------");
		sa.appendLine("// AnalysisEntityCategory");
		for (Type type : analysisContext.getTypes()) {
			sa.appendLine(type.getName());
		}
		sa.appendLine("//----------------------------------------------------");
		sa.appendLine("// Relations");
		for (Relation r : analysisContext.getRelations()) {
			sa.appendLine(String.format("%s (%s) --- (%s) %s", r.getFirst().getName(), (r.getFirstCardinality() != null ? r.getFirstCardinality() : "null"),
				(r.getSecondCardinality() != null ? r.getSecondCardinality() : "null"), r.getSecond().getName()));
		}
		return sa.toString();
		*/
		return "not implemented yet";
	}
}
