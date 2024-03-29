import lombok.Data;
import lombok.Getter;
import org.lwjgl.Version;

import java.awt.geom.Point2D;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import vbo.VBO;
import vbo.dd.VBODynamic2D;
import vbo.dd.VBOVertex2DColor3D;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

@Data
public class Main {

    private static Thread thr = new Thread(new UpdateThread());

    private static VBODynamic2D pointsVBO;

    @Getter
    private static VBODynamic2D additionalVBO;

    private static List<VBOVertex2DColor3D> buildCircle(int[] array) {
        List<VBOVertex2DColor3D> list = new ArrayList<>();
        double currentX;
        double currentY;
        double currentAngle;
        double angle = 360.0d/(double)array.length;
        double index = 0d;
        double multiplier;
        for(int i : array) {
            currentAngle = Math.toRadians(index++ * angle);
            currentX = Math.cos(currentAngle);
            currentY = Math.sin(currentAngle);
            multiplier = (double)i/(double)array.length;
            currentX *= multiplier;
            currentY *= multiplier;
            list.add(new VBOVertex2DColor3D(currentX, currentY));
        }
        return list;
    }

    public static List<VBOVertex2DColor3D> pointList = new CopyOnWriteArrayList<>();
    public static List<VBOVertex2DColor3D> additionalList = new CopyOnWriteArrayList<>();

    private static boolean sorted;

    public static int[] array = new int[120];

    public static int[] colorArray;

    public static void bubbleSort(int[] arr, BiConsumer<Integer, Integer> onIterationCallback)
    {
        int n = arr.length;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                onIterationCallback.accept(j, j + 1);
                if (arr[j] > arr[j + 1]) {
                    // swap arr[j+1] and arr[i]
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    onIterationCallback.accept(j + 1, j);
                }
            }
        }
    }

    private static void generateArray(int[] arr) {
        List<Integer> list = new ArrayList<>();
        int curr = 1;
        for(int i : arr) {
            list.add(curr++);
        }
        Collections.shuffle(list);
        for(int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
    }

    // The window handle
    private static long window;

    public static void run() throws Exception {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void updatePointList() {
        pointList = buildCircle(array);
    }

    private static void init() {
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

        // Create the window
        window = glfwCreateWindow(1024, 768, "Hello World!", NULL, NULL);
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

        generateArray(array);
        pointList = buildCircle(array);
    }

    private static void loop() throws Exception{
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        pointsVBO = new VBODynamic2D();
        additionalVBO = new VBODynamic2D();
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        glfwSetWindowSizeCallback(window, (win, width, height) -> {
            glViewport(0, 0, width, height);
        });
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            //glMatrixMode(GL_PROJECTION);
            //glFrustum(-2.0d, 2.0d, -2.0d, 2.0d, -2.0d, 2.0d );
            //glMatrixMode(GL_MODELVIEW);
            render();
            glfwSwapBuffers(window); // swap the color buffers
            if(!sorted) {
                sorted = true;
                thr.start();
            }
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    private static void render() throws Exception {
        pointsVBO.fillBuffer(Util.pointListToDoubleArray(pointList));
        pointsVBO.draw(GL_LINE_LOOP, pointList.size());
        additionalVBO.fillBuffer(Util.pointListToDoubleArray(additionalList));
        additionalVBO.draw(GL_LINES, additionalList.size());
    }

    public static void main(String[] args) throws Exception {
        run();
    }
}
