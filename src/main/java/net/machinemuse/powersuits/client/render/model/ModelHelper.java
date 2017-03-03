package net.machinemuse.powersuits.client.render.model;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import net.machinemuse.numina.geometry.Colour;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class ModelHelper {
    /*
      * This is a slightly modified version of Forge's example (@author shadekiller666) for the Tesseract model.
      * With this we can generate an extended blockstate to get the quads of any group in a model without
      * having to rebake the model. In this perticular case, the setup is for gettting an extended state that
      * will hide all groups but one. However, this can easily be altered to hide fewer parts if needed.
      *
     */
    @Nullable
    public static IExtendedBlockState getStateForPart(String shownIn, OBJModel.OBJBakedModel objBakedModelIn) {
        BlockStateContainer stateContainer = new ExtendedBlockState(null, new IProperty[0], new IUnlistedProperty[] {net.minecraftforge.common.property.Properties.AnimationProperty});
        List<String> hidden = new ArrayList<>(objBakedModelIn.getModel().getMatLib().getGroups().keySet());
        hidden.remove(shownIn);

        try {
            IModelState state = new IModelState() {
                private final Optional<TRSRTransformation> value = Optional.of(TRSRTransformation.identity());

                @Override
                public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part) {
                    if (part.isPresent()) {
                        UnmodifiableIterator<String> parts = Models.getParts(part.get());
                        if (parts.hasNext()) {
                            String name = parts.next();
                            // only interested in the root level
                            if (!parts.hasNext() && hidden.contains(name)) return value;
                        }
                    }
                    return Optional.absent();
                }
            };
            return ((IExtendedBlockState)stateContainer.getBaseState()).withProperty(net.minecraftforge.common.property.Properties.AnimationProperty, state);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




//    /**
//     * We need our own because the default set is based on the default=facing north
//     * Our model is default facing up
//     */
//    public static TRSRTransformation getLuxCapacitorBlockTransform(EnumFacing side) {
//        Matrix4f matrix;
//
//        switch(side.getOpposite()) {
//            case DOWN:
//                matrix = TRSRTransformation.identity().getMatrix();
//                matrix.setTranslation(new Vector3f(0.0f, -0.4f, 0.0f));
//                break;
//            case UP:
//                matrix = ModelRotation.X180_Y0.getMatrix();
//                matrix.setTranslation(new Vector3f(0.0f, 0.4f, 0.0f));
//                break;
//            case NORTH:
//                matrix = ModelRotation.X90_Y0.getMatrix();
//                matrix.setTranslation(new Vector3f(0.0f, 0.0f, -0.4f));
//                break;
//            case SOUTH:
//                matrix = ModelRotation.X270_Y0.getMatrix();
//                matrix.setTranslation(new Vector3f(0.0f, 0.0f, 0.4f));
//                break;
//            case WEST:
//                matrix = ModelRotation.X90_Y270.getMatrix();
//                matrix.setTranslation(new Vector3f(-0.4f, 0.0f, -0.0f));
//                break;
//            case EAST:
//                matrix = ModelRotation.X90_Y90.getMatrix();
//                break;
//            default:
//                matrix = new Matrix4f();
//                break;
//        }
//        matrix.setScale(0.0625f);
//        return new TRSRTransformation(matrix);
//    }



    /*
     * Here we can color the quads using the setup below. This is better than changing material colors
     * for Wavefront models because it means that you can use a single material for the entire model
     * instead of unique ones for each group. It also means you don't nescessarily need a Wavefront model.
     */
    public static List<BakedQuad> getColoredQuads(List<BakedQuad> quadList, Colour color) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        for (BakedQuad quad : quadList) {
            builder.add(colorQuad(color, quad));
        }
        return builder.build();
    }

//slimeknights.mantle.client.ModelHelper;
    public static BakedQuad colorQuad(Colour color, BakedQuad quad) {
        ColorTransformer transformer = new ColorTransformer(color, quad.getFormat());
        quad.pipe(transformer);
        return transformer.build();
    }

    private static class ColorTransformer extends VertexTransformer {
        private final float r,g,b,a;

        public ColorTransformer(Colour color, VertexFormat format) {
            super(new UnpackedBakedQuad.Builder(format));
            this.r = (float)color.r;
            this.g = (float)color.g;
            this.b = (float)color.b;
            this.a = (float)color.a;
        }

        @Override
        public void put(int element, float... data) {
            VertexFormatElement.EnumUsage usage = parent.getVertexFormat().getElement(element).getUsage();
            // transform normals and position
            if(usage == VertexFormatElement.EnumUsage.COLOR && data.length >= 4) {
                data[0] = r;
                data[1] = g;
                data[2] = b;
                data[3] = a;
            }
            super.put(element, data);
        }

        public UnpackedBakedQuad build() {
            return ((UnpackedBakedQuad.Builder) parent).build();
        }
    }
}