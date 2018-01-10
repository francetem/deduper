package org.ehu.dedupe.graph;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Vertex<T> {

    private final T id;
    private final VertexSet<T> neighbours;

    public Vertex(T id) {
        this.id = id;
        neighbours = new VertexSet<T>();
    }

    public VertexSet<T> neighbourSet() {
        return neighbours;
    }

    public void addNeighbour(Vertex<T> vertex) {
        neighbours.add(vertex);
        if (!vertex.contains(this)) {
            vertex.addNeighbour(this);
        }
    }

    private boolean contains(Vertex<T> tVertex) {
        return neighbours.contains(tVertex);
    }

    public T getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Vertex<?> vertex = (Vertex<?>) o;

        return new EqualsBuilder()
                .append(id, vertex.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
