package ca.bradj.eurekacraft.world.structure.structures;

import ca.bradj.eurekacraft.EurekaCraft;

// TODO: Reimplement
public class TallShack { //extends Structure<NoFeatureConfig> {
    public TallShack() {
        // super(NoFeatureConfig.CODEC);
    }

//    @Override
//    public GenerationStage.Decoration step() {
//        return GenerationStage.Decoration.SURFACE_STRUCTURES;
//    }
//
//    @Override
//    protected boolean isFeatureChunk(
//            ChunkGenerator chunkGen, BiomeProvider p_230363_2_, long p_230363_3_, SharedSeedRandom p_230363_5_,
//            int chunkX, int chunkZ, Biome p_230363_8_, ChunkPos p_230363_9_, NoFeatureConfig p_230363_10_
//    ) {
//        BlockPos center = new BlockPos((chunkX << 4) + 7, 0, (chunkZ << 4));
//        int landHeight = chunkGen.getBaseHeight(center.getX(), center.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
//
//        IBlockReader column = chunkGen.getBaseColumn(center.getX(), center.getZ());
//        BlockState topBlock = column.getBlockState(center.above(landHeight));
//
//        return topBlock.getFluidState().isEmpty();
//    }
//
//    @Override
//    public IStartFactory<NoFeatureConfig> getStartFactory() {
//        return TallShack.Start::new;
//    }
//
//    public static class Start extends StructureStart<NoFeatureConfig> {
//
//        public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox box, int ref, long seed) {
//            super(structure, chunkX, chunkZ, box, ref, seed);
//        }
//
//        @Override
//        public void generatePieces(DynamicRegistries drMan, ChunkGenerator chunkGen, TemplateManager templateMan, int chunkX, int chunkZ, Biome p_230364_6_, NoFeatureConfig p_230364_7_) {
//            // Turns the chunk coords into actual coords we can use
//            int x = (chunkX << 4) + 7;
//            int z = (chunkZ << 4) + 7;
//            BlockPos blockPos = new BlockPos(x, 0, z);
//
//            Supplier<JigsawPattern> supplier = () -> drMan.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(EurekaCraft.MODID, "tall_shack/start_pool"));
//            JigsawManager.addPieces(drMan, new VillageConfig(supplier, 10), AbstractVillagePiece::new, chunkGen, templateMan, blockPos, this.pieces, this.random, false, true);
//
//            // Since by default, the start piece of a structure spawns with it's corner at centerPos
//            // and will randomly rotate around that corner, we will center the piece on centerPos instead.
//            // This is so that our structure's start piece is now centered on the water check done in isFeatureChunk.
//            // Whatever the offset done to center the start piece, that offset is applied to all other pieces
//            // so the entire structure is shifted properly to the new spot.
//            Vector3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
//            int xOffset = blockPos.getX() - structureCenter.getX();
//            int zOffset = blockPos.getZ() - structureCenter.getZ();
//            for(StructurePiece structurePiece : this.pieces){
//                structurePiece.move(xOffset, 0, zOffset);
//            }
//            this.calculateBoundingBox();
//        }
//    }
}
