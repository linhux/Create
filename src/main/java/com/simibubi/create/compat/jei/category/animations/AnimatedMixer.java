package com.simibubi.create.compat.jei.category.animations;

import com.mojang.blaze3d.platform.GlStateManager;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.gui.ScreenElementRenderer;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.IBakedModel;

public class AnimatedMixer extends AnimatedKinetics {

	@Override
	public int getWidth() {
		return 50;
	}

	@Override
	public int getHeight() {
		return 150;
	}

	@Override
	public void draw(int xOffset, int yOffset) {
		GlStateManager.pushMatrix();
		GlStateManager.enableDepthTest();
		GlStateManager.translatef(xOffset, yOffset, 0);
		GlStateManager.rotatef(-15.5f, 1, 0, 0);
		GlStateManager.rotatef(22.5f, 0, 1, 0);
		GlStateManager.translatef(-45, -5, 0);
		GlStateManager.scaled(.45f, .45f, .45f);

		GlStateManager.pushMatrix();
		ScreenElementRenderer.renderModel(this::cogwheel);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		ScreenElementRenderer.renderBlock(this::body);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		ScreenElementRenderer.renderModel(this::pole);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		ScreenElementRenderer.renderModel(this::head);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		ScreenElementRenderer.renderBlock(this::basin);
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	private IBakedModel cogwheel() {
		float t = 25;
		GlStateManager.translatef(t, -t, -t);
		GlStateManager.rotated(getCurrentAngle() * 2, 0, 1, 0);
		GlStateManager.translatef(-t, t, t);
		return AllBlockPartials.SHAFTLESS_COGWHEEL.get();
	}

	private BlockState body() {
		return AllBlocks.MECHANICAL_MIXER.get().getDefaultState();
	}

	private IBakedModel pole() {
		GlStateManager.translatef(0, 51, 0);
		return AllBlockPartials.MECHANICAL_MIXER_POLE.get();
	}

	private IBakedModel head() {
		float t = 25;
		GlStateManager.translatef(0, 51, 0);
		GlStateManager.translatef(t, -t, -t);
		GlStateManager.rotated(getCurrentAngle() * 4, 0, 1, 0);
		GlStateManager.translatef(-t, t, t);
		return AllBlockPartials.MECHANICAL_MIXER_HEAD.get();
	}

	private BlockState basin() {
		GlStateManager.translatef(0, 85, 0);
		return AllBlocks.BASIN.get().getDefaultState();
	}
}
