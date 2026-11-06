package com.example.ui.components.graphics3d

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

enum class Sacred3DSymbolType {
    COPTIC_CROSS,
    INCENSE_CENSER,
    CHURCH_BELL,
    ALTAR_ICON_FRAME
}

/**
 * Lightweight, hardware-accelerated OpenGL ES 3.0 3D Rendering Engine for Jetpack Compose.
 * Renders 3D sacred symbols with GLSL vertex/fragment shaders, specular lighting,
 * depth buffering, and real-time interactive rotation matrices.
 */
class Gl3DSacredRenderer(
    private val context: Context,
    var symbolType: Sacred3DSymbolType = Sacred3DSymbolType.COPTIC_CROSS
) : GLSurfaceView.Renderer {

    private val TAG = "Gl3DSacredRenderer"

    // 3D Matrices
    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private val normalMatrix = FloatArray(16)

    // Interactive Rotation Angles & Scale
    var rotX: Float = 15f
    var rotY: Float = 25f
    var scale: Float = 1.0f

    // Light Source Position (x, y, z, w)
    var lightX: Float = 3f
    var lightY: Float = 5f
    var lightZ: Float = 4f

    private var programId: Int = 0
    private var mvpMatrixHandle: Int = -1
    private var modelMatrixHandle: Int = -1
    private var lightPosHandle: Int = -1
    private var positionHandle: Int = -1
    private var normalHandle: Int = -1
    private var colorHandle: Int = -1

    // Mesh Buffers
    private var currentVertexBuffer: FloatBuffer? = null
    private var currentNormalBuffer: FloatBuffer? = null
    private var currentVertexCount: Int = 0

    // Ambient Floating Angle
    private var floatAngle: Float = 0f

    // GLSL Shaders
    private val vertexShaderCode = """
        #version 300 es
        layout(location = 0) in vec4 aPosition;
        layout(location = 1) in vec3 aNormal;

        uniform mat4 uMVPMatrix;
        uniform mat4 uModelMatrix;

        out vec3 vPosition;
        out vec3 vNormal;

        void main() {
            vPosition = vec3(uModelMatrix * aPosition);
            vNormal = mat3(uModelMatrix) * aNormal;
            gl_Position = uMVPMatrix * aPosition;
        }
    """.trimIndent()

    private val fragmentShaderCode = """
        #version 300 es
        precision mediump float;

        in vec3 vPosition;
        in vec3 vNormal;

        uniform vec3 uLightPos;
        uniform vec4 uColor;

        out vec4 fragColor;

        void main() {
            // Ambient Lighting
            float ambientStrength = 0.35;
            vec3 ambient = ambientStrength * uColor.rgb;

            // Diffuse Lighting
            vec3 norm = normalize(vNormal);
            vec3 lightDir = normalize(uLightPos - vPosition);
            float diff = max(dot(norm, lightDir), 0.0);
            vec3 diffuse = diff * uColor.rgb * 0.85;

            // Specular Lighting (Gold/Metallic Shininess)
            float specularStrength = 0.6;
            vec3 viewDir = normalize(-vPosition);
            vec3 reflectDir = reflect(-lightDir, norm);
            float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
            vec3 specular = specularStrength * vec3(1.0, 0.95, 0.8) * spec;

            vec3 result = ambient + diffuse + specular;
            fragColor = vec4(result, uColor.a);
        }
    """.trimIndent()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.08f, 0.06f, 0.09f, 1.0f) // Dark sacred sanctuary background
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glDepthFunc(GLES30.GL_LEQUAL)

        // Compile GLSL Program
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        programId = GLES30.glCreateProgram().also {
            GLES30.glAttachShader(it, vertexShader)
            GLES30.glAttachShader(it, fragmentShader)
            GLES30.glLinkProgram(it)
        }

        mvpMatrixHandle = GLES30.glGetUniformLocation(programId, "uMVPMatrix")
        modelMatrixHandle = GLES30.glGetUniformLocation(programId, "uModelMatrix")
        lightPosHandle = GLES30.glGetUniformLocation(programId, "uLightPos")
        colorHandle = GLES30.glGetUniformLocation(programId, "uColor")

        positionHandle = GLES30.glGetAttribLocation(programId, "aPosition")
        normalHandle = GLES30.glGetAttribLocation(programId, "aNormal")

        updateMeshGeometry()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val aspect = width.toFloat() / height.toFloat()
        Matrix.perspectiveM(projectionMatrix, 0, 45f, aspect, 0.1f, 100f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        if (programId == 0 || currentVertexBuffer == null) return

        GLES30.glUseProgram(programId)

        // Camera LookAt
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 6.5f, 0f, 0f, 0f, 0f, 1f, 0f)

        // Model Transformations
        floatAngle += 0.8f
        val floatOffsetY = (sin(Math.toRadians(floatAngle.toDouble())) * 0.08f).toFloat()

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, floatOffsetY, 0f)
        Matrix.rotateM(modelMatrix, 0, rotX, 1f, 0f, 0f)
        Matrix.rotateM(modelMatrix, 0, rotY, 0f, 1f, 0f)
        Matrix.scaleM(modelMatrix, 0, scale, scale, scale)

        // MVP Matrix calculation
        val tempMatrix = FloatArray(16)
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, tempMatrix, 0)

        GLES30.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        GLES30.glUniformMatrix4fv(modelMatrixHandle, 1, false, modelMatrix, 0)

        // Pass Light Position
        GLES30.glUniform3f(lightPosHandle, lightX, lightY, lightZ)

        // Color setup (Metallic Gold / Sacred Bronze)
        val mainColor = when (symbolType) {
            Sacred3DSymbolType.COPTIC_CROSS -> floatArrayOf(0.95f, 0.78f, 0.22f, 1.0f) // Sacred Gold
            Sacred3DSymbolType.INCENSE_CENSER -> floatArrayOf(0.85f, 0.65f, 0.25f, 1.0f) // Bronze Censer
            Sacred3DSymbolType.CHURCH_BELL -> floatArrayOf(0.9f, 0.7f, 0.3f, 1.0f) // Golden Bronze Bell
            Sacred3DSymbolType.ALTAR_ICON_FRAME -> floatArrayOf(0.88f, 0.72f, 0.28f, 1.0f) // Gold Icon Frame
        }
        GLES30.glUniform4fv(colorHandle, 1, mainColor, 0)

        // Bind Vertex Positions
        currentVertexBuffer?.position(0)
        GLES30.glEnableVertexAttribArray(positionHandle)
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 0, currentVertexBuffer)

        // Bind Normals
        currentNormalBuffer?.position(0)
        GLES30.glEnableVertexAttribArray(normalHandle)
        GLES30.glVertexAttribPointer(normalHandle, 3, GLES30.GL_FLOAT, false, 0, currentNormalBuffer)

        // Draw Triangles
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, currentVertexCount)

        GLES30.glDisableVertexAttribArray(positionHandle)
        GLES30.glDisableVertexAttribArray(normalHandle)
    }

    fun updateMeshGeometry() {
        val (vertices, normals) = when (symbolType) {
            Sacred3DSymbolType.COPTIC_CROSS -> generate3DCopticCrossMesh()
            Sacred3DSymbolType.INCENSE_CENSER -> generate3DCenserMesh()
            Sacred3DSymbolType.CHURCH_BELL -> generate3DChurchBellMesh()
            Sacred3DSymbolType.ALTAR_ICON_FRAME -> generate3DIconFrameMesh()
        }

        currentVertexCount = vertices.size / 3

        currentVertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(vertices)
                position(0)
            }

        currentNormalBuffer = ByteBuffer.allocateDirect(normals.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(normals)
                position(0)
            }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES30.glCreateShader(type).also { shader ->
            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 3D PROCEDURAL MESH GENERATORS (3D Geometries & Normals)
    // ─────────────────────────────────────────────────────────────────────────────

    private fun generate3DCopticCrossMesh(): Pair<FloatArray, FloatArray> {
        val vList = mutableListOf<Float>()
        val nList = mutableListOf<Float>()

        val armW = 0.35f
        val armL = 1.2f
        val depth = 0.22f

        // Box 1: Vertical Arm
        add3DBox(vList, nList, 0f, 0f, 0f, armW, armL, depth)
        // Box 2: Horizontal Arm
        add3DBox(vList, nList, 0f, 0f, 0f, armL, armW, depth)

        // Central Ring
        val segments = 24
        val innerR = 0.35f
        val outerR = 0.52f
        for (i in 0 until segments) {
            val a1 = (i * 2 * Math.PI / segments).toFloat()
            val a2 = ((i + 1) * 2 * Math.PI / segments).toFloat()

            val x1i = innerR * cos(a1); val y1i = innerR * sin(a1)
            val x1o = outerR * cos(a1); val y1o = outerR * sin(a1)
            val x2i = innerR * cos(a2); val y2i = innerR * sin(a2)
            val x2o = outerR * cos(a2); val y2o = outerR * sin(a2)

            // Front Ring Face
            addTriangle(vList, nList,
                x1i, y1i, depth/2, x1o, y1o, depth/2, x2o, y2o, depth/2,
                0f, 0f, 1f
            )
            addTriangle(vList, nList,
                x1i, y1i, depth/2, x2o, y2o, depth/2, x2i, y2i, depth/2,
                0f, 0f, 1f
            )
        }

        return Pair(vList.toFloatArray(), nList.toFloatArray())
    }

    private fun generate3DCenserMesh(): Pair<FloatArray, FloatArray> {
        val vList = mutableListOf<Float>()
        val nList = mutableListOf<Float>()

        val segments = 20
        val rBottom = 0.25f
        val rMid = 0.65f
        val rTop = 0.45f
        val hBase = -0.6f
        val hMid = 0.0f
        val hLid = 0.55f

        // Censer Bowl (Cone Frustum)
        for (i in 0 until segments) {
            val a1 = (i * 2 * Math.PI / segments).toFloat()
            val a2 = ((i + 1) * 2 * Math.PI / segments).toFloat()

            val x1b = rBottom * cos(a1); val z1b = rBottom * sin(a1)
            val x2b = rBottom * cos(a2); val z2b = rBottom * sin(a2)
            val x1m = rMid * cos(a1); val z1m = rMid * sin(a1)
            val x2m = rMid * cos(a2); val z2m = rMid * sin(a2)

            addTriangle(vList, nList, x1b, hBase, z1b, x1m, hMid, z1m, x2m, hMid, z2m, x1m, 0.5f, z1m)
            addTriangle(vList, nList, x1b, hBase, z1b, x2m, hMid, z2m, x2b, hBase, z2b, x2m, 0.5f, z2m)
        }

        // Censer Dome Lid
        for (i in 0 until segments) {
            val a1 = (i * 2 * Math.PI / segments).toFloat()
            val a2 = ((i + 1) * 2 * Math.PI / segments).toFloat()

            val x1m = rMid * cos(a1); val z1m = rMid * sin(a1)
            val x2m = rMid * cos(a2); val z2m = rMid * sin(a2)

            addTriangle(vList, nList, x1m, hMid, z1m, 0f, hLid, 0f, x2m, hMid, z2m, x1m, 0.8f, z1m)
        }

        return Pair(vList.toFloatArray(), nList.toFloatArray())
    }

    private fun generate3DChurchBellMesh(): Pair<FloatArray, FloatArray> {
        val vList = mutableListOf<Float>()
        val nList = mutableListOf<Float>()

        val slices = 20
        val rings = 8
        val height = 1.2f

        for (r in 0 until rings) {
            val y1 = (r.toFloat() / rings) * height - (height / 2f)
            val y2 = ((r + 1).toFloat() / rings) * height - (height / 2f)

            // Bell flare profile radius calculation
            val radius1 = 0.25f + 0.45f * (1.0f - (r.toFloat() / rings)) * (1.0f - (r.toFloat() / rings))
            val radius2 = 0.25f + 0.45f * (1.0f - ((r + 1).toFloat() / rings)) * (1.0f - ((r + 1).toFloat() / rings))

            for (s in 0 until slices) {
                val a1 = (s * 2 * Math.PI / slices).toFloat()
                val a2 = ((s + 1) * 2 * Math.PI / slices).toFloat()

                val x11 = radius1 * cos(a1); val z11 = radius1 * sin(a1)
                val x12 = radius1 * cos(a2); val z12 = radius1 * sin(a2)
                val x21 = radius2 * cos(a1); val z21 = radius2 * sin(a1)
                val x22 = radius2 * cos(a2); val z22 = radius2 * sin(a2)

                addTriangle(vList, nList, x11, y1, z11, x21, y2, z21, x22, y2, z22, x11, 0.2f, z11)
                addTriangle(vList, nList, x11, y1, z11, x22, y2, z22, x12, y1, z12, x12, 0.2f, z12)
            }
        }

        return Pair(vList.toFloatArray(), nList.toFloatArray())
    }

    private fun generate3DIconFrameMesh(): Pair<FloatArray, FloatArray> {
        val vList = mutableListOf<Float>()
        val nList = mutableListOf<Float>()

        val w = 1.0f
        val h = 1.3f
        val border = 0.18f
        val depth = 0.2f

        // Outer Frame Box
        add3DBox(vList, nList, 0f, 0f, 0f, w, h, depth)

        return Pair(vList.toFloatArray(), nList.toFloatArray())
    }

    private fun add3DBox(
        vList: MutableList<Float>,
        nList: MutableList<Float>,
        cx: Float, cy: Float, cz: Float,
        w: Float, h: Float, d: Float
    ) {
        val x1 = cx - w/2f; val x2 = cx + w/2f
        val y1 = cy - h/2f; val y2 = cy + h/2f
        val z1 = cz - d/2f; val z2 = cz + d/2f

        // Front Face (z = z2)
        addTriangle(vList, nList, x1, y1, z2, x2, y1, z2, x2, y2, z2, 0f, 0f, 1f)
        addTriangle(vList, nList, x1, y1, z2, x2, y2, z2, x1, y2, z2, 0f, 0f, 1f)

        // Back Face (z = z1)
        addTriangle(vList, nList, x2, y1, z1, x1, y1, z1, x1, y2, z1, 0f, 0f, -1f)
        addTriangle(vList, nList, x2, y1, z1, x1, y2, z1, x2, y2, z1, 0f, 0f, -1f)

        // Left Face (x = x1)
        addTriangle(vList, nList, x1, y1, z1, x1, y1, z2, x1, y2, z2, -1f, 0f, 0f)
        addTriangle(vList, nList, x1, y1, z1, x1, y2, z2, x1, y2, z1, -1f, 0f, 0f)

        // Right Face (x = x2)
        addTriangle(vList, nList, x2, y1, z2, x2, y1, z1, x2, y2, z1, 1f, 0f, 0f)
        addTriangle(vList, nList, x2, y1, z2, x2, y2, z1, x2, y2, z2, 1f, 0f, 0f)

        // Top Face (y = y2)
        addTriangle(vList, nList, x1, y2, z2, x2, y2, z2, x2, y2, z1, 0f, 1f, 0f)
        addTriangle(vList, nList, x1, y2, z2, x2, y2, z1, x1, y2, z1, 0f, 1f, 0f)

        // Bottom Face (y = y1)
        addTriangle(vList, nList, x1, y1, z1, x2, y1, z1, x2, y1, z2, 0f, -1f, 0f)
        addTriangle(vList, nList, x1, y1, z1, x2, y1, z2, x1, y1, z2, 0f, -1f, 0f)
    }

    private fun addTriangle(
        vList: MutableList<Float>, nList: MutableList<Float>,
        x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float,
        x3: Float, y3: Float, z3: Float,
        nx: Float, ny: Float, nz: Float
    ) {
        vList.add(x1); vList.add(y1); vList.add(z1)
        vList.add(x2); vList.add(y2); vList.add(z2)
        vList.add(x3); vList.add(y3); vList.add(z3)

        val len = Math.sqrt((nx * nx + ny * ny + nz * nz).toDouble()).toFloat().coerceAtLeast(0.001f)
        val fnx = nx / len; val fny = ny / len; val fnz = nz / len

        nList.add(fnx); nList.add(fny); nList.add(fnz)
        nList.add(fnx); nList.add(fny); nList.add(fnz)
        nList.add(fnx); nList.add(fny); nList.add(fnz)
    }
}
