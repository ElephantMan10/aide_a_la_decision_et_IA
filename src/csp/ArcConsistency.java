package csp;

import representation.*;
import java.util.*;

public class ArcConsistency {
    protected Set<Constraint> constraints;
    public ArcConsistency(Set<Constraint> constraints) {
        for(Constraint cons : constraints)
            if(cons.getScope().size() > 2)
                throw new IllegalArgumentException("Given constraint is neither binary or unary.");
        this.constraints = constraints;
    }

    public boolean enforceNodeConsistency(Map<Variable, Set<Object>> vardom) {
        Map<Variable,Set<Object>> futureDel = new HashMap<>();
        for(Constraint cons: this.constraints) {
            if(cons instanceof UnaryConstraint) {
                UnaryConstraint u = (UnaryConstraint)cons;
                if(futureDel.get(u.getVariable()) == null) {
                    futureDel.put(u.getVariable(), new HashSet<>());
                    futureDel.get(u.getVariable()).addAll(u.getDomain());
                } else {
                    futureDel.get(u.getVariable()).retainAll(u.getDomain());
                }
            }
        }

        for(Variable var: futureDel.keySet()) {
            Set<Object> tmp = vardom.get(var);
            tmp.retainAll(futureDel.get(vardom));
            if(tmp.size() == 0) {
                return false;
            }
        }

        return true;
    }
}
