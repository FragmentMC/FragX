#version 330

layout (location = 0) in vec2 i_pos;
layout (location = 1) in vec2 i_uv;

uniform mat4 u_projection;

out vec2 textureCoord;

void main() {
    gl_Position = u_projection * vec4(i_pos, 0.0f, 1.0f);

    textureCoord = i_uv;
}

