# PSC2MA
## Description
Abstract—Unpredictability and uncertainty about future evolutions of both the system and its environment may easily compromise the
behaviour of the system. The subsequent software failures can have serious consequences. When dealing with open environments, run-time
monitoring is one of the most promising techniques to detect software failures. Several monitoring approaches have been proposed in the last
years; however, they suffer from two main limitations. First, they provide limited information to be exploited at run-time for early detecting and
managing situations that most probably will lead to failures. Second, they mainly rely on logic-based specifications, which intrinsic complexity
might hamper the use of these monitoring approaches in industrial contexts.

In order to address these two limitations, this paper proposes a new approach, called PREDIMO (PREDIctive MOnitoring), which, starting from
scenario-based specifications, automatically generates predictive monitors that taking into account the actual status and also possible
evolutions of both system and environment in the near future identify risky situations thus enabling the definition of precise strategies to
prevent failures. More specifically, the generated monitors evaluate the specified properties and return one of these seven values: satisfied,
infinitely controllable, system finitely controllable, system urgently controllable, environment finitely controllable, environment urgently
controllable, and violated.

The scenario-based notation of PREDIMO is based on the property sequence charts language that facilitates the non trivial and error prone
task of specifying, correctly and without expertise in temporal logic, temporal properties.

The correctness of the translation process from the properties specification to the monitors is formally proven (see the Appendix). The overall
approach is tool supported and a large experimentation with OSGi (Open Service Gateway Initiative) applications demonstrates its feasibility
and usability.
