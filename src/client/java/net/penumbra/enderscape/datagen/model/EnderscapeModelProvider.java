package net.penumbra.enderscape.datagen.model;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VaultBlock;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.block.EnderscapeBlockFamilies;
import net.penumbra.enderscape.block.state.EndHavenCoreState;
import net.penumbra.enderscape.block.state.OptionalMagniaPolarityProperty;
import net.penumbra.enderscape.block.state.PurifyingPhase;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.item.component.RubbleShieldVariant;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import net.penumbra.enderscape.renderer.item.DyeColorItemModelProperty;
import net.penumbra.enderscape.renderer.item.EnabledItemModelProperty;
import net.penumbra.enderscape.renderer.item.RubbleShieldVariantItemModelProperty;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.data.models.BlockModelGenerators.*;
import static net.minecraft.client.data.models.model.ModelLocationUtils.getModelLocation;
import static net.minecraft.client.data.models.model.ModelTemplates.*;
import static net.minecraft.world.level.block.Blocks.END_STONE;
import static net.penumbra.enderscape.datagen.model.EnderscapeModelTemplates.*;
import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.*;

public class EnderscapeModelProvider extends FabricModelProvider {

    private static final PropertyDispatch<VariantMutator> ROTATIONS_COLUMN_WITH_FACING = PropertyDispatch.modify(StateProperties.FACING)
            .select(Direction.DOWN, X_ROT_180)
            .select(Direction.UP, NOP)
            .select(Direction.NORTH, X_ROT_90)
            .select(Direction.SOUTH, X_ROT_90.then(Y_ROT_180))
            .select(Direction.WEST, X_ROT_90.then(Y_ROT_270))
            .select(Direction.EAST, X_ROT_90.then(Y_ROT_90));

    private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING = PropertyDispatch.modify(StateProperties.HORIZONTAL_FACING)
            .select(Direction.EAST, Y_ROT_90)
            .select(Direction.SOUTH, Y_ROT_180)
            .select(Direction.WEST, Y_ROT_270)
            .select(Direction.NORTH, NOP);

    private static final PropertyDispatch<VariantMutator> ROTATION_TORCH = PropertyDispatch.modify(StateProperties.HORIZONTAL_FACING)
            .select(Direction.EAST, NOP)
            .select(Direction.SOUTH, Y_ROT_90)
            .select(Direction.WEST, Y_ROT_180)
            .select(Direction.NORTH, Y_ROT_270);

    private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING_ALT = PropertyDispatch.modify(StateProperties.HORIZONTAL_FACING)
            .select(Direction.SOUTH, NOP)
            .select(Direction.WEST, Y_ROT_90)
            .select(Direction.NORTH, Y_ROT_180)
            .select(Direction.EAST, Y_ROT_270);

    public static final List<ModelTemplate> BRACKET_MODEL_TEMPLATES = List.of(TEMPLATE_BRACKET_1, TEMPLATE_BRACKET_2, TEMPLATE_BRACKET_3, TEMPLATE_BRACKET_4);
    public static final List<ModelTemplate> OVERGROWTH_MODEL_TEMPLATES = List.of(TEMPLATE_OVERGROWTH_1, TEMPLATE_OVERGROWTH_2, TEMPLATE_OVERGROWTH_3, TEMPLATE_OVERGROWTH_4);
    public static final List<ModelTemplate> PATH_MODEL_TEMPLATES = List.of(TEMPLATE_PATH_1, TEMPLATE_PATH_2, TEMPLATE_PATH_3, TEMPLATE_PATH_4);

    public EnderscapeModelProvider(FabricPackOutput output) {
        super(output);
    }

    /*
        Block states and block models
     */

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        EnderscapeBlockFamilies.getAllFamilies().filter(BlockFamily::shouldGenerateModel).forEach(family -> generators.family(family.getBaseBlock()).generateFor(family));

        createNoOminousVault(generators, END_VAULT);
        createNoOminousTrialSpawner(generators, END_TRIAL_SPAWNER);
        createEndHavenCore(generators, END_HAVEN_CORE);

        generators.blockStateOutput.accept(createSimpleBlock(VOID_LACHRYMA_CAULDRON, plainVariant(
                ModelTemplates.CAULDRON_FULL.create(VOID_LACHRYMA_CAULDRON, TextureMapping.cauldron(TextureMapping.getBlockTexture(VOID_LACHRYMA, "_still")), generators.modelOutput)))
        );

        createBasicInternalModel(generators, DRIFT_JELLY_BLOCK);

        createPottablePlant(generators, DRY_END_GROWTH, POTTED_DRY_END_GROWTH, true, true);
        createPottablePlant(generators, CHORUS_SPROUTS, POTTED_CHORUS_SPROUTS, true, true);

        generators.createTrivialCube(ALLURING_MAGNIA);
        generators.createTrivialCube(REPULSIVE_MAGNIA);
        createBlisteredMagnia(generators, BLISTERED_MAGNIA);
        createPolarizedMagnia(generators, POLARIZED_MAGNIA);

        createMagniaSprout(generators, ALLURING_MAGNIA_SPROUT, POTTED_ALLURING_MAGNIA_SPROUT);
        createMagniaSprout(generators, REPULSIVE_MAGNIA_SPROUT, POTTED_REPULSIVE_MAGNIA_SPROUT);
        createMagniaRadio(generators, MAGNIA_RADIO);

        createVoidShale(generators, VOID_SHALE);
        createVoidTorch(generators, VOID_TORCH, VOID_WALL_TORCH);
        createLantern(generators, VOID_LANTERN);
        createVoidCampfire(generators, VOID_CAMPFIRE);

        generators.createTrivialCube(SHADOLINE_ORE);
        generators.createTrivialCube(MIRESTONE_SHADOLINE_ORE);
        generators.createTrivialCube(RAW_SHADOLINE_BLOCK);
        generators.createBarsAndItem(SHADOLINE_BARS);
        generators.createAxisAlignedPillarBlockCustomModel(SHADOLINE_CHAIN, plainVariant(TexturedModel.CHAIN.create(SHADOLINE_CHAIN, generators.modelOutput)));
        generators.registerSimpleFlatItemModel(SHADOLINE_CHAIN.asItem());
        generators.createRotatedPillarWithHorizontalVariant(SHADOLINE_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);

        createNebuliteOre(generators, NEBULITE_ORE);
        createNebuliteOre(generators, MIRESTONE_NEBULITE_ORE);
        createBasicInternalModel(generators, NEBULITE_BLOCK);

        generators.createRotatedPillarWithHorizontalVariant(DUSK_PURPUR_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
        generators.createTrivialCube(END_LAMP);

        createOvergrowth(generators, CELESTIAL_OVERGROWTH, END_STONE, OVERGROWTH_MODEL_TEMPLATES);
        createOvergrowth(generators, CORRUPT_OVERGROWTH, MIRESTONE, OVERGROWTH_MODEL_TEMPLATES);
        createOvergrowth(generators, VEILED_END_STONE, END_STONE, OVERGROWTH_MODEL_TEMPLATES);

        createOvergrowth(generators, CELESTIAL_PATH, END_STONE, PATH_MODEL_TEMPLATES);
        createOvergrowth(generators, CORRUPT_PATH, MIRESTONE, PATH_MODEL_TEMPLATES);

        generators.createTrivialCube(CELESTIAL_CAP);
        generators.createTrivialCube(MURUBLIGHT_CAP);

        createPlant(generators, WISP_SPROUTS, true);
        createPottablePlant(generators, WISP_GROWTH, POTTED_WISP_GROWTH, true, true);
        createPottablePlant(generators, VEILED_SAPLING, POTTED_VEILED_SAPLING, true, false);
        generators.createTrivialBlock(VEILED_LEAVES, TexturedModel.LEAVES);
        createVeiledVines(generators, VEILED_VINES);
        
        createEmissiveGrowth(generators, CELESTIAL_GROWTH, POTTED_CELESTIAL_GROWTH);
        createEmissiveGrowth(generators, CORRUPT_GROWTH, POTTED_CORRUPT_GROWTH);
        createPottablePlant(generators, CELESTIAL_CHANTERELLE, POTTED_CELESTIAL_CHANTERELLE, true, false);
        createDirectionalPottablePlant(generators, MURUBLIGHT_CHANTERELLE, POTTED_MURUBLIGHT_CHANTERELLE, true, false);
        createBulbFlower(generators, BULB_FLOWER, POTTED_BULB_FLOWER);

        createLantern(generators, BULB_LANTERN);
        createPuruberryVine(generators, PURUBERRY_VINE);

        createBlinklightVines(generators, BLINKLIGHT_VINES_BODY, BLINKLIGHT_VINES_HEAD);
        createBasicInternalModel(generators, POTTED_BLINKLIGHT);
        createBlinklamp(generators, BLINKLAMP);
        createBracket(generators, MURUBLIGHT_BRACKET, BRACKET_MODEL_TEMPLATES);

        generators.woodProvider(VEILED_LOG).log(VEILED_LOG).wood(VEILED_WOOD);
        generators.woodProvider(CELESTIAL_STEM).log(CELESTIAL_STEM).wood(CELESTIAL_HYPHAE);
        generators.woodProvider(MURUBLIGHT_STEM).log(MURUBLIGHT_STEM).wood(MURUBLIGHT_HYPHAE);

        generators.woodProvider(STRIPPED_VEILED_LOG).log(STRIPPED_VEILED_LOG).wood(STRIPPED_VEILED_WOOD);
        generators.woodProvider(STRIPPED_CELESTIAL_STEM).log(STRIPPED_CELESTIAL_STEM).wood(STRIPPED_CELESTIAL_HYPHAE);
        generators.woodProvider(STRIPPED_MURUBLIGHT_STEM).log(STRIPPED_MURUBLIGHT_STEM).wood(STRIPPED_MURUBLIGHT_HYPHAE);

        generators.createHangingSign(STRIPPED_VEILED_LOG, VEILED_HANGING_SIGN, VEILED_WALL_HANGING_SIGN);
        generators.createHangingSign(STRIPPED_CELESTIAL_STEM, CELESTIAL_HANGING_SIGN, CELESTIAL_WALL_HANGING_SIGN);
        generators.createHangingSign(STRIPPED_MURUBLIGHT_STEM, MURUBLIGHT_HANGING_SIGN, MURUBLIGHT_WALL_HANGING_SIGN);

        generators.createShelf(VEILED_SHELF, STRIPPED_VEILED_LOG);
        generators.createShelf(CELESTIAL_SHELF, STRIPPED_CELESTIAL_STEM);
        generators.createShelf(MURUBLIGHT_SHELF, STRIPPED_MURUBLIGHT_STEM);

        createChorusCakeRoll(generators, CHORUS_CAKE_ROLL);
    }

    private void createBasicInternalModel(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(createSimpleBlock(block, internalModel(block)));
    }

    private void createNebuliteOre(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(
                createSimpleBlock(block, plainVariant(
                        TEMPLATE_NEBULITE_ORE.create(block, EnderscapeTextureMapping.nebuliteOre(NEBULITE_ORE, block), generators.modelOutput))
                )
        );
    }

    private void createEmissiveOvergrownBricks(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(
                createSimpleBlock(block, plainVariant(
                        TEMPLATE_EMISSIVE_OVERGROWN_BRICKS.create(block, EnderscapeTextureMapping.emissiveAllTextured(block), generators.modelOutput))
                )
        );
    }

    private void createBlisteredMagnia(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(
                        PropertyDispatch.initial(StateProperties.OPTIONAL_MAGNIA_POLARITY).generate(
                                value -> {
                                    String suffix = !value.equals(OptionalMagniaPolarityProperty.NONE) ? "_" + value.getSerializedName() : "";
                                    return plainVariant(
                                            generators.createSuffixedVariant(block, suffix, CUBE_ALL, TextureMapping::cube)
                                    );
                                }
                        )
                )
        );
    }

    private void createPolarizedMagnia(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(
                        PropertyDispatch.initial(StateProperties.MAGNIA_POLARITY, StateProperties.POWERED)
                                .generate((value, powered) -> {
                                    String suffix = "_" + value.getSerializedName() + (powered ? "_powered" : "");
                                    return plainVariant(
                                            generators.createSuffixedVariant(block, suffix, CUBE_ALL, TextureMapping::cube)
                                    );
                                })
                )
        );
        generators.registerSimpleItemModel(block, getModelLocation(block, "_alluring"));
    }

    private void createBlinklightVines(BlockModelGenerators generators, Block body, Block head) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(body)
                .with(
                        PropertyDispatch.initial(StateProperties.BLINKLIGHT_BODY_STAGE).generate(
                                value -> plainVariant(generators.createSuffixedVariant(body, "_" + value, TINTED_CROSS, TextureMapping::cross)
                                )
                        )
                )
        );

        for (int i = 0; i < 4; i++) {
            String suffix = "_" + i;
            TINTED_CROSS.create(getModelLocation(head, suffix), EnderscapeTextureMapping.crossSuffixed(head, suffix), generators.modelOutput);
        }

        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(head)
                .with(PropertyDispatch.initial(StateProperties.BLINKLIGHT_HEAD_STAGE)
                        .select(0, blinklightVariant(head, 0))
                        .select(1, blinklightVariant(head, 1))
                        .select(2, blinklightVariant(head, 2))
                        .select(3, blinklightVariant(head, 3))
                        .select(4, blinklightVariant(head, 2))
                        .select(5, blinklightVariant(head, 1))
                )
        );
    }

    private MultiVariant blinklightVariant(Block block, int value) {
        return plainVariant(getModelLocation(block, "_" + value));
    }

    private void createBlinklamp(BlockModelGenerators generators, Block block) {
        for (int i = 0; i < 5; i++) {
            String suffix = "_luminance" + i;
            CUBE_ALL.create(getModelLocation(block, suffix), EnderscapeTextureMapping.blinklamp(block, suffix), generators.modelOutput);
        }

        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(StateProperties.BLINKLAMP_LUMINANCE)
                        .select(0, blinklampVariant(block, 0))
                        .select(1, blinklampVariant(block, 1))
                        .select(2, blinklampVariant(block, 1))
                        .select(3, blinklampVariant(block, 2))
                        .select(4, blinklampVariant(block, 2))
                        .select(5, blinklampVariant(block, 3))
                        .select(6, blinklampVariant(block, 3))
                        .select(7, blinklampVariant(block, 4))
                )
        );

        generators.registerSimpleItemModel(block, blinklampModel(block, 4));
    }

    private MultiVariant blinklampVariant(Block block, int value) {
        return plainVariant(blinklampModel(block, value));
    }

    private static Identifier blinklampModel(Block block, int value) {
        return getModelLocation(block, "_luminance" + value);
    }

    private void createVoidShale(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(
                        PropertyDispatch.initial(StateProperties.VOID_SHALE_STRESS).generate(
                                value -> plainVariant(
                                        generators.createSuffixedVariant(
                                                block, "_stress" + value, TEMPLATE_VOID_SHALE,
                                                (material) -> EnderscapeTextureMapping.voidShale(block, value > 0 ? "_stress" + value : "")
                                        )
                                )
                        )
                )
        );
        generators.registerSimpleItemModel(block, getModelLocation(block, "_stress0"));
    }

    private void createVoidTorch(BlockModelGenerators generators, Block torch, Block wallTorch) {
        createBasicInternalModel(generators, torch);
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(wallTorch, internalModel(wallTorch)).with(ROTATION_TORCH));
        generators.registerSimpleFlatItemModel(torch);
    }

    private void createLantern(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(createBooleanModelDispatch(
                        StateProperties.HANGING,
                        internalModel(block, "_hanging"),
                        internalModel(block)
                )));

        generators.registerSimpleFlatItemModel(block.asItem());
    }

    private void createVoidCampfire(BlockModelGenerators generators, Block block) {
        MultiVariant offModel = plainVariant(ModelLocationUtils.decorateBlockModelLocation("campfire_off"));

        generators.blockStateOutput
                .accept(
                        MultiVariantGenerator.dispatch(block)
                                .with(createBooleanModelDispatch(
                                        StateProperties.LIT,
                                        internalModel(block),
                                        offModel)
                                )
                                .with(ROTATION_HORIZONTAL_FACING_ALT)
                );

        generators.registerSimpleFlatItemModel(block.asItem());
    }

    private void createChorusCakeRoll(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(StateProperties.BITES)
                        .select(0, plainVariant(getModelLocation(block)))
                        .select(1, plainVariant(getModelLocation(block, "_bites1")))
                        .select(2, plainVariant(getModelLocation(block, "_bites2")))
                        .select(3, plainVariant(getModelLocation(block, "_bites3")))
                        .select(4, plainVariant(getModelLocation(block, "_bites4")))
                        .select(5, plainVariant(getModelLocation(block, "_bites5")))
                        .select(6, plainVariant(getModelLocation(block, "_bites6")))
                ).with(ROTATION_HORIZONTAL_FACING)
        );
    }

    private void createOvergrowth(BlockModelGenerators generators, Block block, Block baseStone, List<ModelTemplate> templates) {
        List<Variant> variants = new ArrayList<>(List.of());

        for (int i = 0; i < templates.size(); i++) {
            variants.add(plainModel(templates.get(i).createWithSuffix(block, "_" + i, EnderscapeTextureMapping.overgrowth(block, baseStone), generators.modelOutput)));
        }

        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(
                block,
                variants(variants.toArray(new Variant[0]))).with(ROTATIONS_COLUMN_WITH_FACING)
        );

        generators.registerSimpleItemModel(block, variants.getFirst().modelLocation());
    }

    private void createVeiledVines(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(
                        PropertyDispatch.initial(StateProperties.GROWTH_PART).generate(
                                part -> {
                                    String suffix = "_" + part.getSerializedName();
                                    return plainVariant(generators.createSuffixedVariant(
                                            block, suffix, TEMPLATE_VEILED_VINES,
                                            (material) -> EnderscapeTextureMapping.crossSuffixed(block, suffix)
                                    ));
                                })
                ).with(ROTATIONS_COLUMN_WITH_FACING)
        );

        Material material = new Material(BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_top"));
        Identifier model = FLAT_ITEM.create(getModelLocation(block), new TextureMapping().put(TextureSlot.LAYER0, material), generators.modelOutput);
        generators.registerSimpleItemModel(block, model);
    }

    private void createEmissiveGrowth(BlockModelGenerators generators, Block block, Block potted) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(
                        PropertyDispatch.initial(StateProperties.GROWTH_PART).generate(
                                part -> {
                                    String suffix = "_" + part.getSerializedName();
                                    return plainVariant(generators.createSuffixedVariant(
                                            block, suffix, TEMPLATE_EMISSIVE_GROWTH,
                                            (material) -> EnderscapeTextureMapping.emissiveCross(block, suffix)
                                    ));
                                })
                ).with(ROTATIONS_COLUMN_WITH_FACING)
        );

        createBasicInternalModel(generators, potted);

        Material material = new Material(BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_top"));
        Identifier model = FLAT_ITEM.create(getModelLocation(block), new TextureMapping().put(TextureSlot.LAYER0, material), generators.modelOutput);
        generators.registerSimpleItemModel(block, model);
    }

    private void createBracket(BlockModelGenerators generators, Block block, List<ModelTemplate> templates) {
        List<Variant> variants = new ArrayList<>(List.of());

        for (int i = 0; i < templates.size(); i++) {
            variants.add(plainModel(templates.get(i).createWithSuffix(block, "_" + i, TextureMapping.cube(block), generators.modelOutput)));
        }

        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(
                block,
                variants(variants.toArray(new Variant[0]))).with(ROTATIONS_COLUMN_WITH_FACING)
        );

        generators.registerSimpleFlatItemModel(block.asItem());
    }

    private void createPuruberryVine(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(
                        PropertyDispatch.initial(StateProperties.ATTACHED).generate(
                                attached -> {
                                    String suffix = attached ? "_attached" : "";
                                    return plainVariant(generators.createSuffixedVariant(
                                            block, suffix, CROSS, (material) -> EnderscapeTextureMapping.crossSuffixed(block, suffix)
                                    ));
                                })
                )
        );
    }

    private void createMagniaSprout(BlockModelGenerators generators, Block block, Block potted) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(
                        PropertyDispatch.initial(StateProperties.POWERED).generate(
                                powered -> {
                                    String suffix = powered ? "_powered" : "";
                                    return plainVariant(generators.createSuffixedVariant(
                                            block, suffix, powered ? TEMPLATE_MAGNIA_SPROUT_POWERED : CROSS,
                                            (material) -> powered ? EnderscapeTextureMapping.emissiveCross(block, suffix) : TextureMapping.cross(block)
                                    ));
                                })
                ).with(ROTATIONS_COLUMN_WITH_FACING)
        );

        MultiVariant model = plainVariant(FLOWER_POT_CROSS.create(potted, EnderscapeTextureMapping.plant(block, "_potted"), generators.modelOutput));
        generators.blockStateOutput.accept(createSimpleBlock(potted, model));

        generators.registerSimpleFlatItemModel(block);
    }

    private void createMagniaRadio(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(
                        PropertyDispatch.initial(StateProperties.ENABLED).generate(
                                enabled -> {
                                    String suffix = enabled ? "_enabled" : "_disabled";
                                    return plainVariant(generators.createSuffixedVariant(
                                            block, suffix, TEMPLATE_MAGNIA_RADIO, (material) -> EnderscapeTextureMapping.magniaRadio(block, suffix)
                                    ));
                                })
                ).with(ROTATION_HORIZONTAL_FACING)
        );

        generators.registerSimpleFlatItemModel(block.asItem());
    }

    private void createNoOminousVault(BlockModelGenerators generators, Block block) {
        TextureMapping inactiveTextures = TextureMapping.vault(block, "_front_off", "_side_off", "_top", "_bottom");
        TextureMapping activeTextures = TextureMapping.vault(block, "_front_on", "_side_on", "_top", "_bottom");
        TextureMapping unlockingTextures = TextureMapping.vault(block, "_front_ejecting", "_side_on", "_top", "_bottom");
        TextureMapping ejectingRewardTextures = TextureMapping.vault(block, "_front_ejecting", "_side_on", "_top_ejecting", "_bottom");

        Identifier inactiveModel = ModelTemplates.VAULT.create(block, inactiveTextures, generators.modelOutput);

        MultiVariant inactive = plainVariant(inactiveModel);
        MultiVariant active = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_active", activeTextures, generators.modelOutput));
        MultiVariant unlocking = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_unlocking", unlockingTextures, generators.modelOutput));
        MultiVariant ejectingReward = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_ejecting_reward", ejectingRewardTextures, generators.modelOutput));

        generators.registerSimpleItemModel(block, inactiveModel);
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(VaultBlock.STATE).generate((state) -> switch (state) {
            case INACTIVE -> inactive;
            case ACTIVE -> active;
            case UNLOCKING -> unlocking;
            case EJECTING -> ejectingReward;
        })).with(ROTATION_HORIZONTAL_FACING));
    }

    private void createNoOminousTrialSpawner(BlockModelGenerators generators, Block block) {
        TextureMapping inactiveTextures = TextureMapping.trialSpawner(block, "_side_inactive", "_top_inactive");
        TextureMapping activeTextures = TextureMapping.trialSpawner(block, "_side_active", "_top_active");
        TextureMapping ejectingRewardTextures = TextureMapping.trialSpawner(block, "_side_active", "_top_ejecting_reward");

        Identifier inactiveModel = ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.create(block, inactiveTextures, generators.modelOutput);
        MultiVariant inactive = plainVariant(inactiveModel);
        MultiVariant active = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_active", activeTextures, generators.modelOutput));
        MultiVariant ejectingReward = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_ejecting_reward", ejectingRewardTextures, generators.modelOutput));

        generators.registerSimpleItemModel(block, inactiveModel);
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(StateProperties.TRIAL_SPAWNER_STATE).generate((state) -> switch (state) {
            case INACTIVE, COOLDOWN -> inactive;
            case WAITING_FOR_PLAYERS, ACTIVE, WAITING_FOR_REWARD_EJECTION -> active;
            case EJECTING_REWARD -> ejectingReward;
        })));
    }

    private void createEndHavenCore(BlockModelGenerators generators, Block block) {
        Identifier inactiveModel = getModelLocation(block, "_inactive");

        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(StateProperties.END_HAVEN_CORE_STATE)
                .select(EndHavenCoreState.INACTIVE, plainVariant(inactiveModel))
                .select(EndHavenCoreState.ACTIVE, plainVariant(getModelLocation(block, "_active")))
        ));

        generators.registerSimpleItemModel(block, inactiveModel);
    }

    private void createBulbFlower(BlockModelGenerators generators, Block block, Block potted) {
        Identifier model = getModelLocation(block);

        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(StateProperties.PURIFYING_PHASE)
                .select(PurifyingPhase.POWERLESS, plainVariant(model))
                .select(PurifyingPhase.INACTIVE, plainVariant(model))
                .select(PurifyingPhase.CHARGING, plainVariant(model.withSuffix("_charging")))
                .select(PurifyingPhase.ACTIVE, plainVariant(model.withSuffix("_active")))
                .select(PurifyingPhase.COOLDOWN, plainVariant(model))
        ));

        generators.registerSimpleFlatItemModel(block);
        createBasicInternalModel(generators, potted);
    }

    private void createDirectionalPottablePlant(BlockModelGenerators generators, Block block, Block potted, boolean generateModel, boolean suffix) {
        createDirectionalBlock(generators, block, generateModel);

        if (generateModel) {
            createPottedPlant(generators, block, potted, suffix);
            generators.registerSimpleFlatItemModel(block);
        } else {
            createBasicInternalModel(generators, potted);
        }
    }

    private void createDirectionalBlock(BlockModelGenerators generators, Block block, boolean generateModel) {
        MultiVariant model;

        if (generateModel) {
            model = plainVariant(ModelTemplates.CROSS.create(block, TextureMapping.cross(block), generators.modelOutput));
        } else {
            model = internalModel(block);
        }

        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, model).with(ROTATIONS_COLUMN_WITH_FACING));
    }

    private void createPlant(BlockModelGenerators generators, Block block, boolean generateModel) {
        if (generateModel) {
            generators.createCrossBlockWithDefaultItem(block, BlockModelGenerators.PlantType.NOT_TINTED);
        } else {
            createBasicInternalModel(generators, block);
            generators.registerSimpleFlatItemModel(block);
        }
    }

    private void createPottablePlant(BlockModelGenerators generators, Block block, Block potted, boolean generateModel, boolean suffix) {
        createPlant(generators, block, generateModel);

        if (generateModel) {
            createPottedPlant(generators, block, potted, suffix);
        } else {
            createBasicInternalModel(generators, potted);
        }
    }

    private void createPottedPlant(BlockModelGenerators generators, Block block, Block potted, boolean suffix) {
        createPottedPlant(generators, TextureMapping.getBlockTexture(block, suffix ? "_potted" : ""), potted);
    }

    private void createPottedPlant(BlockModelGenerators generators, Material material, Block potted) {
        TextureMapping textures = TextureMapping.plant(material);
        MultiVariant model = plainVariant(PlantType.NOT_TINTED.getCrossPot().create(potted, textures, generators.modelOutput));
        generators.blockStateOutput.accept(createSimpleBlock(potted, model));
    }

    private static @NonNull MultiVariant internalModel(Block block) {
        return plainVariant(getModelLocation(block));
    }

    private static @NonNull MultiVariant internalModel(Block block, String suffix) {
        return plainVariant(getModelLocation(block, suffix));
    }

    /*
        Item Models
     */

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(EnderscapeItems.DAGGER, FLAT_HANDHELD_ITEM);

        createMagniaAttractor(generators, EnderscapeItems.MAGNIA_ATTRACTOR);
        createRubbleShield(generators, EnderscapeItems.RUBBLE_SHIELD);
        createMirror(generators, EnderscapeItems.MIRROR);

        generators.generateFlatItem(EnderscapeItems.RUBBLEMITE_SPAWN_EGG, FLAT_ITEM);
        generators.generateFlatItem(EnderscapeItems.RUSTLE_SPAWN_EGG, FLAT_ITEM);
        generators.generateFlatItem(EnderscapeItems.DRIFTER_SPAWN_EGG, FLAT_ITEM);

        generators.generateFlatItem(EnderscapeItems.RUBBLE_CHITIN, FLAT_ITEM);
        generators.generateFlatItem(EnderscapeItems.RUSTLE_SILK, FLAT_ITEM);
        generators.generateFlatItem(EnderscapeItems.STASIS_ARMOR_TRIM_SMITHING_TEMPLATE, FLAT_ITEM);
        generators.generateFlatItem(EnderscapeItems.END_CITY_KEY, FLAT_ITEM);

        generators.generateFlatItem(EnderscapeItems.RUSTLE_BUCKET, FLAT_ITEM);
        generators.generateFlatItem(EnderscapeItems.VOID_LACHRYMA_BUCKET, FLAT_ITEM);
        generators.generateFlatItem(EnderscapeItems.DRIFT_JELLY_BOTTLE, FLAT_ITEM);

        generators.generateFlatItem(EnderscapeItems.MUSIC_DISC_GLARE, MUSIC_DISC);
        generators.generateFlatItem(EnderscapeItems.MUSIC_DISC_DECAY, MUSIC_DISC);
        generators.generateFlatItem(EnderscapeItems.MUSIC_DISC_BLISS, MUSIC_DISC);

        generators.generateFlatItem(EnderscapeItems.CHORUS_CAKE_ROLL_ITEM, FLAT_ITEM);

        generators.generateFlatItem(EnderscapeItems.RAW_SHADOLINE, FLAT_ITEM);
        generators.generateFlatItem(EnderscapeItems.SHADOLINE_INGOT, FLAT_ITEM);
        generators.generateFlatItem(EnderscapeItems.SHADOLINE_NUGGET, FLAT_ITEM);

        generators.generateFlatItem(EnderscapeItems.NEBULITE_SHARDS, FLAT_ITEM);
        generators.generateFlatItem(EnderscapeItems.NEBULITE, FLAT_ITEM);

        generators.generateFlatItem(EnderscapeItems.PURUBERRY, FLAT_ITEM);
        generators.generateFlatItem(EnderscapeItems.BLINKLIGHT, FLAT_ITEM);

        generators.generateFlatItem(EnderscapeItems.HEALING, FLAT_ITEM);
    }

    private void createMirror(ItemModelGenerators generators, Item item) {
        Material baseLayer = TextureMapping.getItemTexture(item);
        Identifier plainModel = TEMPLATE_MIRROR.create(item, TextureMapping.layer0(baseLayer), generators.modelOutput);

        List<SelectItemModel.SwitchCase<DyeColor>> cases = new ArrayList<>(DyeColor.values().length);
        for (DyeColor color : DyeColor.VALUES) {
            String name = color.getSerializedName();

            cases.add(ItemModelUtils.when(color, ItemModelUtils.plainModel(
                    TEMPLATE_DYED_MIRROR.create(
                            ModelLocationUtils.getModelLocation(item, "_" + name),
                            TextureMapping.layered(baseLayer, TextureMapping.getItemTexture(item, "_overlay_" + name)), generators.modelOutput
                    )
            )));
        }

        generators.itemModelOutput.accept(
                item,
                ItemModelUtils.select(
                        new DyeColorItemModelProperty(),
                        ItemModelUtils.plainModel(plainModel),
                        cases
                )
        );
    }

    private void createRubbleShield(ItemModelGenerators generators, Item item) {
        List<SelectItemModel.SwitchCase<Identifier>> cases = new ArrayList<>(RubbleShieldVariant.VARIANTS.size());

        for (Identifier variant : RubbleShieldVariant.VARIANTS) {
            cases.add(ItemModelUtils.when(variant, ItemModelUtils.conditional(
                    ItemModelUtils.isUsingItem(),
                    ItemModelUtils.plainModel(
                            TEMPLATE_RUBBLE_SHIELD_USING.create(
                                    ModelLocationUtils.getModelLocation(item, "_" + variant.getPath() + "_using"),
                                    TextureMapping.layer0(TextureMapping.getItemTexture(item, "_" + variant.getPath())),
                                    generators.modelOutput
                            )
                    ),
                    ItemModelUtils.plainModel(
                            generators.createFlatItemModel(
                                    item,
                                    "_" + variant.getPath(),
                                    TEMPLATE_RUBBLE_SHIELD
                            )
                    )
            )));
        }

        generators.itemModelOutput.accept(
                item,
                ItemModelUtils.select(
                        new RubbleShieldVariantItemModelProperty(),
                        ItemModelUtils.conditional(
                                ItemModelUtils.isUsingItem(),
                                ItemModelUtils.plainModel(getItemModelLocation("rubble_shield_end_stone_using")),
                                ItemModelUtils.plainModel(getItemModelLocation("rubble_shield_end_stone"))
                        ),
                        cases
                )
        );
    }

    private void createMagniaAttractor(ItemModelGenerators generators, Item item) {
        ItemModel.Unbaked normalModel = ItemModelUtils.plainModel(generators.createFlatItemModel(item, TEMPLATE_MAGNIA_ATTRACTOR));
        ItemModel.Unbaked enabledModel = ItemModelUtils.plainModel(generators.createFlatItemModel(item, "_enabled", TEMPLATE_MAGNIA_ATTRACTOR));

        generators.itemModelOutput.accept(
                item,
                ItemModelUtils.conditional(
                        new EnabledItemModelProperty(),
                        enabledModel,
                        normalModel
                )
        );
    }

    public static Identifier getItemModelLocation(String path) {
        return Enderscape.id(path).withPrefix("item/");
    }
}