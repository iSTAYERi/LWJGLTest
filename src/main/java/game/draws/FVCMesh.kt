package game.draws

import engine.graph.Mesh

class FVCMesh{

    var mesh: Mesh
    var positions: FloatArray
    var indices: IntArray
    var colours: FloatArray

    init {
        positions = floatArrayOf(
                -0.5f,  0.5f, -1.05f,
                -0.5f, -0.5f, -1.05f,
                0.5f, -0.5f, -1.05f,
                0.5f,  0.5f, -1.05f
        )

        indices = intArrayOf(0, 1, 3, 3, 1, 2)

        colours = floatArrayOf(
                0.5f, 0.0f, 0.0f,
                0.0f,0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f
        )
        mesh = Mesh(positions, colours, indices)
    }

}