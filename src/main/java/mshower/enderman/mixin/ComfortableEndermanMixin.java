package mshower.enderman.mixin;

import mshower.enderman.ComfortableEnderman;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.*;

@Mixin(targets = "net.minecraft.entity.mob.EndermanEntity$PickUpBlockGoal")
public abstract class ComfortableEndermanMixin {

    @Shadow @Final
    private EndermanEntity enderman;

    @Unique
    private static final TagKey<Block> ENDERMAN_BLACKLIST =
            TagKey.of(RegistryKeys.BLOCK,
                    Identifier.of(ComfortableEnderman.MOD_ID, "enderman_blacklist"));

    /**
     * @author MeteorOfTime
     * @reason Let enderman not pick sth up.
     */
    @Overwrite
    public void tick() {
        Random random = this.enderman.getRandom();
        World world = this.enderman.getWorld();

        int i = MathHelper.floor(this.enderman.getX() - 2.0 + random.nextDouble() * 4.0);
        int j = MathHelper.floor(this.enderman.getY() + random.nextDouble() * 3.0);
        int k = MathHelper.floor(this.enderman.getZ() - 2.0 + random.nextDouble() * 4.0);

        BlockPos blockPos = new BlockPos(i, j, k);
        BlockState blockState = world.getBlockState(blockPos);

        Vec3d from = new Vec3d(this.enderman.getBlockX() + 0.5, j + 0.5, this.enderman.getBlockZ() + 0.5);
        Vec3d to = new Vec3d(i + 0.5, j + 0.5, k + 0.5);

        BlockHitResult hit = world.raycast(new RaycastContext(from, to,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                this.enderman));

        if (hit.getBlockPos().equals(blockPos)
                && blockState.isIn(BlockTags.ENDERMAN_HOLDABLE)
                && !blockState.isIn(ENDERMAN_BLACKLIST)) {
            world.removeBlock(blockPos, false);
            world.emitGameEvent(GameEvent.BLOCK_DESTROY, blockPos,
                    GameEvent.Emitter.of(this.enderman, blockState));
            this.enderman.setCarriedBlock(blockState.getBlock().getDefaultState());
        }
    }
}