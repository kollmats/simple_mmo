#version 330

in vec3 ourColor;
out vec4 color;

void main()
{
    color = vec4(ourColor, 0.5f);
//    color = vec4(0.5f, 0.5f, 0.5f, 0.0f);
}