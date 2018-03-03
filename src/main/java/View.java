package simple_mmo;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL11.*;

import com.google.common.io.Resources;
import java.nio.charset.StandardCharsets;
import java.net.URL;

public class View {

	// The window handle
	private long window;
	private WorldMap worldMap;

	public void setWorldMap(WorldMap wm) {
		synchronized(this) {
			worldMap = wm;
		}
	}

	public WorldMap getWorldMap() {
		synchronized(this) {
			return worldMap;
		}
	}

	public void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default

		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// The following four badboys are needed for macOS and openGL 3.0+: http://www.glfw.org/faq.html#how-do-i-create-an-opengl-30-context
		glfwWindowHint (GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint (GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint (GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		// Create the window
		window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public void render() throws Exception{
		int vaoId;
		int vboId;
		int eboId;

		final int nRows = worldMap.getRows();
		final int nCols = worldMap.getColumns();

		final float rowSize = 2.0f / nRows;
		final float colSize = 2.0f / nCols;

		final int nTiles    = nRows * nCols;
		final int nVertices = nTiles * 6 * 3; // 6 vertices per tile

		float[] vertices = new float[3 * nVertices];
		float[] colors = new float[nVertices * 3];
		WorldMap worldMap = getWorldMap();

		int idxTile = 0;
		for (int i = 0; i < nRows; i++) {
			
			final float y1 =  1.0f - i * rowSize;
			final float y2 =  y1;
			final float y3 =  1.0f - (1 + i) * rowSize;
			
			final float y4 =  y3;
			final float y5 =  y1;
			final float y6 =  y3;						
			
			for (int j = 0; j < nCols; j++) {
				
				final float x1 = -1.0f + j * colSize;
				final float x2 = -1.0f + (1 + j) * colSize;
				final float x3 =  x1;
				
				final float x4 =  x3;
				final float x5 =  x2;
				final float x6 =  x2;
				
				int idx = idxTile * 18; // 3 coords * 6 vertices per tile.

				// These guys represent 2 triangles.
				vertices[idx     ] = x1;
				vertices[idx +  1] = y1;
				vertices[idx +  2] = 0.0f;
				
				vertices[idx +  3] = x2;
				vertices[idx +  4] = y2;
				vertices[idx +  5] = 0.0f;
				
				vertices[idx +  6] = x3;
				vertices[idx +  7] = y3;
				vertices[idx +  8] = 0.0f;
				
				vertices[idx +  9] = x4;
				vertices[idx + 10] = y4;
				vertices[idx + 11] = 0.0f;

				vertices[idx + 12] = x5;
				vertices[idx + 13] = y5;
				vertices[idx + 14] = 0.0f;
				
				vertices[idx + 15] = x6;
				vertices[idx + 16] = y6;
				vertices[idx + 17] = 0.0f;

				for (int k = 0; k < 6; k++) {
					int l = idx + k * 3;
					if (worldMap.getEntity(i, j, 0, 0).isPresent()) {
						colors[l    ] = 0.0f;				
						colors[l + 1] = 0.5f;
						colors[l + 2] = 0.0f;
					} 
				}

				idxTile += 1;
			}
		}

		/*
		for (int i = 0; i < nVertices; i++) {
			int idx = i * 3;
			colors[idx    ] = 0.0f;				
			colors[idx + 1] = 0.5f;
			colors[idx + 2] = 0.0f;
			}*/

		int vertexArrayId; // This is the guy we bind our color and vertex buffers to.
		int vertexBufferId;
		int colorBufferId;
		
		//int x = 1 / 0;			
        FloatBuffer vertexBuffer = null;
        FloatBuffer colorBuffer = null;		
        try {			
            // Create the VAO and bind to it
            vertexArrayId = glGenVertexArrays();
			glBindVertexArray(vertexArrayId);
			
			// ------ CREATE THE VERTICES STUFF
            vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
            vertexBuffer.put(vertices).flip();
            // Create a VBO and bint to the VAO
			
            vertexBufferId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			// ------ CREATE THE COLORS STUFF
            colorBuffer = MemoryUtil.memAllocFloat(colors.length);
            colorBuffer.put(colors).flip();
			
            // Create a VBO and bint to the VAO
            colorBufferId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, colorBufferId);			
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
			
        } finally {
            if (vertexBuffer != null) {
                MemoryUtil.memFree(vertexBuffer);
            }
            if (colorBuffer != null) {
                MemoryUtil.memFree(colorBuffer);
            }
        }

		ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(loadResource("vertex.vs"));
        shaderProgram.createFragmentShader(loadResource("fragment.fs"));
        shaderProgram.link();		


		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		if ( !shouldClose() ) {
			glClearColor(1.0f, 0.0f, 0.0f, 0.0f); // Clear the color			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			shaderProgram.bind();
			
			glEnableVertexAttribArray(0); // Enable vertices
			glEnableVertexAttribArray(1); // Enable colors

			glDrawArrays(GL_TRIANGLES, 0, nTiles * 2 * 3); // draw 4 tiles * 2 triangles * 3 vertices
			
			glDisableVertexAttribArray(0);
			glDisableVertexAttribArray(1);			
			//glBindVertexArray(0);

			shaderProgram.unbind();

			
			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}
	
	public void cleanUp() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public String loadResource(String fileName) throws Exception {
		URL url = Resources.getResource(fileName);
		String text = Resources.toString(url, StandardCharsets.UTF_8);
		return text;
	}

	public static void main(String[] args) throws Exception {
		
		View view = new View();
		view.init();
		
		WorldMap wm = new WorldMap.Builder(9, 9, 1)
			.placeEntity(0, 0, 0, 0, new Entity())
			.placeEntity(8, 8, 0, 0, new Entity())
			.placeEntity(4, 4, 0, 0, new Entity())
			.build();
		
		view.setWorldMap(wm);


		while (!view.shouldClose()) 
			view.render();
		
		view.cleanUp();
	}

}


