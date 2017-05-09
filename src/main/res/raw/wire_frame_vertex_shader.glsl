uniform mat4 u_Matrix;

attribute vec4 a_Position;

void main() {

    vec4 a = vec4(0.001, 0.001, 0.001, 0.0);
    vec4 b = a + a_Position;
    gl_Position = u_Matrix * b;
}