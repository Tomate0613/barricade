package net.modgarden.barricade.client.model;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.modgarden.barricade.Barricade;
import net.modgarden.barricade.client.BarricadeClient;
import net.modgarden.barricade.component.BlockedDirectionsComponent;
import net.modgarden.barricade.component.BlockedEntitiesComponent;
import net.modgarden.barricade.registry.BarricadeTags;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AdvancedBarrierBlockUnbakedModel extends BlockModel {
    public static final Material BARRIER = new Material(TextureAtlas.LOCATION_BLOCKS, Barricade.asResource("block/barrier"));

    public AdvancedBarrierBlockUnbakedModel(@Nullable BlockedDirectionsComponent directions, @Nullable BlockedEntitiesComponent entities) {
        super(null, constructElements(directions, entities), createTextureMap(entities), false, GuiLight.SIDE, ItemTransforms.NO_TRANSFORMS, List.of());
    }

    private static Map<String, Either<Material, String>> createTextureMap(@Nullable BlockedEntitiesComponent entities) {
        Material innerMaterial = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.withDefaultNamespace("missingno"));
        if (entities != null)
            innerMaterial = new Material(TextureAtlas.LOCATION_BLOCKS, entities.backTextureLocation());
        return Map.of("particle", Either.left(BARRIER), "barrier", Either.left(BARRIER), "inner", Either.left(innerMaterial));
    }

    private static List<BlockElement> constructElements(@Nullable BlockedDirectionsComponent directions, @Nullable BlockedEntitiesComponent entities) {
        if (directions != null && directions.doesNotBlock())
            return List.of();
        Map<Direction, BlockElementFace> faces = new HashMap<>();
        Map<Direction, BlockElementFace> innerFaces = new HashMap<>();
        for (Direction direction : Direction.values()) {
            if (directions == null || directions.blocks(direction)) {
                if (entities != null)
                    innerFaces.put(direction, new BlockElementFace(direction, BlockElementFace.NO_TINT, "inner", new BlockFaceUV(null, 0)));
                faces.put(direction, new BlockElementFace(direction, BlockElementFace.NO_TINT, "barrier", new BlockFaceUV(null, 0)));
            }
        }
        List<BlockElement> returnValue = new ArrayList<>();
        returnValue.add(new BlockElement(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(16.0F, 16.0F, 16.0F), faces, null, true));
        if (!innerFaces.isEmpty())
            returnValue.add(new BlockElement(new Vector3f(0.002F, 0.002F, 0.002F), new Vector3f(15.998F, 15.998F, 15.998F), innerFaces, null, true));
        return returnValue;
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker modelBaker, Function<Material, TextureAtlasSprite> textureGetter, ModelState modelState) {
        return BarricadeClient.getHelper().createCreativeOnlyModel(super.bake(modelBaker, textureGetter, modelState), Either.left(BarricadeTags.ItemTags.BARRIERS));
    }
}
