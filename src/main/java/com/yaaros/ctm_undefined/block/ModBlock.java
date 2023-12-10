package com.yaaros.ctm_undefined.block;

import com.yaaros.ctm_undefined.Main;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlock {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            ForgeRegistries.BLOCKS, Main.MOD_ID);
    public static final BlockBehaviour.Properties A = BlockBehaviour.Properties.of(Material.STONE).
            strength(2f).requiresCorrectToolForDrops();
    public static final RegistryObject<Block> A_Block = BLOCKS.register("a", () -> new Block(A));
}
