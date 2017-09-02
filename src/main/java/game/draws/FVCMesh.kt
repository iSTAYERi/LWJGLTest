package game.draws

import engine.graph.Mesh

class FVCMesh{

    var mesh: Mesh
    var positions: FloatArray
    var indices: IntArray
    var colours: FloatArray

    init {
        positions = floatArrayOf(
                -0.7f,  0.1f, 0.5f,
                -0.7f, -0.1f, 0.5f,
                0.7f, -0.1f, 0.5f,
                0.7f,  0.1f, 0.5f,
                -0.7f,  0.1f, -0.5f,
                -0.7f, -0.1f, -0.5f,
                0.7f, -0.1f, -0.5f,
                0.7f,  0.1f, -0.5f
        )

        indices = intArrayOf(
                // Front Face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 7, 7, 0, 3,
                // Right Face
                3, 2, 7, 7, 2, 6,
                // Left Face
                4, 5, 0, 0, 5, 1,
                // Bottom Face
                1, 5, 2, 2, 5, 6,
                // Back Face
                6, 7, 5, 5, 7, 4
        )

        colours = floatArrayOf(
                75f/255f, 59f/255f, 90f/255f,
                2f/255f, 10f/255f, 10f/255f,
                2f/255f, 10f/255f, 10f/255f,
                75f/255f, 59f/255f, 90f/255f,
                75f/255f, 59f/255f, 90f/255f,
                2f/255f, 10f/255f, 10f/255f,
                2f/255f, 10f/255f, 10f/255f,
                75f/255f, 59f/255f, 90f/255f
        )

        mesh = Mesh(positions, colours, indices)
    }

}