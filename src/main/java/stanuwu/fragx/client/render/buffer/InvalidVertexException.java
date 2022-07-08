package stanuwu.fragx.client.render.buffer;

/**
 * Exception to be thrown if vertices are wrongly constructed in a buffer builder.
 */
public class InvalidVertexException extends Exception {
    public InvalidVertexException(VertexModes vertexMode) {
        super("Invalid Vertex Construct for Mode: " + vertexMode.name());
    }
}
