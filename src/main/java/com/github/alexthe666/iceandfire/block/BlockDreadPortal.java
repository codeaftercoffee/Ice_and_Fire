package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadPortal;
import com.github.alexthe666.iceandfire.world.dimension.TeleporterDreadLands;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDreadPortal extends BlockContainer implements IDreadBlock{

    public BlockDreadPortal() {
        super(Material.PORTAL);
        this.setHardness(-1.0F);
        this.setResistance(10000F);
        this.setLightLevel(0.2F);
        this.setTranslationKey("iceandfire.dread_portal");
        this.setRegistryName(IceAndFire.MODID, "dread_portal");
        this.setTickRandomly(true);
        GameRegistry.registerTileEntity(TileEntityDreadPortal.class, "dread_portal");
    }

    @SideOnly(Side.CLIENT)
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return 15728880;
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if ((!entity.isBeingRidden()) && (entity.getPassengers().isEmpty())) {
            if ((entity instanceof EntityPlayerMP)) {
                EntityPlayerMP thePlayer = (EntityPlayerMP) entity;
                if (thePlayer.timeUntilPortal > 0) {
                    thePlayer.timeUntilPortal = 10;
                } else if (thePlayer.dimension != IceAndFire.CONFIG.dreadlandsDimensionId) {
                    thePlayer.timeUntilPortal = 10;
                    thePlayer.server.getPlayerList().transferPlayerToDimension(thePlayer, IceAndFire.CONFIG.dreadlandsDimensionId, new TeleporterDreadLands(thePlayer.server.getWorld(IceAndFire.CONFIG.dreadlandsDimensionId)));
                } else {
                    thePlayer.timeUntilPortal = 10;
                    thePlayer.server.getPlayerList().transferPlayerToDimension(thePlayer, IceAndFire.CONFIG.dreadlandsDimensionId, new TeleporterDreadLands(thePlayer.server.getWorld(IceAndFire.CONFIG.dreadlandsDimensionId)));
                }
            }
            if (!(entity instanceof EntityPlayer)) {
                if (entity.dimension != IceAndFire.CONFIG.dreadlandsDimensionId) {
                    entity.timeUntilPortal = 10;
                 //   entity.changeDimension(IceAndFire.CONFIG.dreadlandsDimensionId);
                } else {
                    entity.timeUntilPortal = 10;
                   // entity.changeDimension(IceAndFire.CONFIG.dreadlandsDimensionId);
                }
            }
        }
    }


    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public boolean canSurviveAt(World world, BlockPos pos) {
        return true;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDreadPortal();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        return !iblockstate.isOpaqueCube() && block != ModBlocks.dread_portal;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityDreadPortal) {
            int i = 3;
            for (int j = 0; j < i; ++j) {
                double d0 = (double) ((float) pos.getX() + rand.nextFloat());
                double d1 = (double) ((float) pos.getY() + rand.nextFloat());
                double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
                double d3 = ((double) rand.nextFloat() - 0.5D) * 0.25D;
                double d4 = ((double) rand.nextFloat()) * -0.25D;
                double d5 = ((double) rand.nextFloat() - 0.5D) * 0.25D;
                int k = rand.nextInt(2) * 2 - 1;
                IceAndFire.PROXY.spawnParticle("dread_portal", d0, d1, d2, d3, d4, d5);
                //worldIn.spawnParticle(EnumParticleTypes.END_ROD, d0, d1, d2, d3, d4, d5);
            }
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.LIGHT_BLUE;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}