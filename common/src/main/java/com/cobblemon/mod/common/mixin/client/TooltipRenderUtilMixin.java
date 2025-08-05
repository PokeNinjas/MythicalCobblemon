package com.cobblemon.mod.common.mixin.client;

import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TooltipRenderUtil.class)
public class TooltipRenderUtilMixin {
    @ModifyVariable(method = "renderTooltipBackground", at = @At("HEAD"), ordinal = 4, argsOnly = true)
    private static int injected(int z) {
        return 4000;
    }
}
