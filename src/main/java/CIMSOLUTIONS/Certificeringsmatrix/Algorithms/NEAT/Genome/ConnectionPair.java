package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

import java.util.Objects;

/*- This class is a representation of a connection between 2 nodes;
 */
public class ConnectionPair {
    private final int inputNodeId;
    private final int outputNodeId;

    public ConnectionPair(int inputNodeId, int outputNodeId) {
        this.inputNodeId = inputNodeId;
        this.outputNodeId = outputNodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionPair that = (ConnectionPair) o;
        return inputNodeId == that.inputNodeId && outputNodeId == that.outputNodeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputNodeId, outputNodeId);
    }
}
