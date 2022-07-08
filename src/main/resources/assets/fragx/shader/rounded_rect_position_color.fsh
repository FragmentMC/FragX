#version 330

in vec4 vertexColor;

uniform float u_radius;
uniform vec2 u_size;
uniform vec2 u_position;

out vec4 fragmentColor;

float distanceFromCentre(vec2 centre, vec2 size, float radius) {
    return length(max(abs(centre)-size+radius, 0.0f))-radius;
}

void main() {
    if (vertexColor.a == 0.0f) {
        discard;
    }
    float distance = distanceFromCentre(gl_FragCoord.xy - u_position - u_size/2.0f, u_size/2.0f, u_radius);
    float smoothedAlpha = 1.0f-smoothstep(0.0f, 2.0f, distance);
    fragmentColor = vec4(vertexColor.rgb, vertexColor.a * smoothedAlpha);
}
