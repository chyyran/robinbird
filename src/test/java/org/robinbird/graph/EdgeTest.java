package org.robinbird.graph;

import lombok.EqualsAndHashCode;
import org.junit.Test;

/**
 * Created by seokhyun on 11/10/17.
 */
public class EdgeTest {

	@EqualsAndHashCode(callSuper = true)
	public static final class ExtendedEdge extends Edge {
		public ExtendedEdge(Node n1, Node n2) {
			super(n1, n2);
		}
	}

	@Test(expected = NullPointerException.class)
	public void when_null_node_is_given_for_source_exception_is_thrown() {
		new Edge(null, new Node("test"));
	}

	@Test(expected = NullPointerException.class)
	public void when_null_node_is_given_for_target_exception_is_thrown() {
		new Edge(new Node("test"), null);
	}
}
