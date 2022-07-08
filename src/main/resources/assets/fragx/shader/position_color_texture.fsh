#version 330

in vec4 vertexColor;
in vec2 textureCoord;

uniform sampler2D u_sampler;

out vec4 fragmentColor;

void main() {
    vec4 color = texture(u_sampler, textureCoord) * vertexColor;
    if (color.a == 0.0f) {
        discard;
    }
    fragmentColor = color;
}