package mshower.enderman.mixin;

import mshower.enderman.ComfortableEnderman;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;

@Mixin(targets = "net.minecraft.entity.mob.EndermanEntity$PickUpBlockGoal")
public abstract class ComfortableEndermanMixin {

    @Shadow @Final
    private EndermanEntity enderman;

    @Unique
    private static final TagKey<net.minecraft.block.Block> ENDERMAN_BLACKLIST =
            TagKey.of(RegistryKeys.BLOCK, new Identifier(ComfortableEnderman.MOD_ID, "enderman_blacklist"));

    /**
     * @author MeteorOfTime
     * @reason Let enderman not pick sth up.
     */
    @Overwrite
    public void tick() {
        if (enderman.getCarriedBlock() != null) return;

        Random random = enderman.getRandom();
        World world = enderman.getWorld();

        int i = MathHelper.floor(enderman.getX() - 2.0 + random.nextDouble() * 4.0);
        int j = MathHelper.floor(enderman.getY() + random.nextDouble() * 3.0);
        int k = MathHelper.floor(enderman.getZ() - 2.0 + random.nextDouble() * 4.0);
        BlockPos pos = new BlockPos(i, j, k);
        BlockState state = world.getBlockState(pos);
        
        if (state.isIn(net.minecraft.registry.tag.BlockTags.ENDERMAN_HOLDABLE) && !state.isIn(ENDERMAN_BLACKLIST)) {
            Vec3d from = new Vec3d(enderman.getX() + 0.5, j + 0.5, enderman.getZ() + 0.5);
            Vec3d to   = new Vec3d(i + 0.5, j + 0.5, k + 0.5);
            if (world.raycast(new RaycastContext(from, to,
                            RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, enderman))
                    .getBlockPos().equals(pos)) {
                world.removeBlock(pos, false);
                enderman.setCarriedBlock(state.getBlock().getDefaultState());
            }
        }
    }
}