package org.robinbird.clustering;

import java.util.List;
import org.robinbird.model.Component;

public interface ClusteringMethod {

    List<ClusteringNode> cluster(List<Component> components, RelationsSelector relationsSelector, ClusteringNodeMatcher nodeMatcher);

}
