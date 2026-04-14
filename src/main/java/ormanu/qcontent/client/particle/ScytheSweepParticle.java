package ormanu.qcontent.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class ScytheSweepParticle extends SingleQuadParticle {
    private final SpriteSet sprites;


    private ScytheSweepParticle(final ClientLevel level, final double x, final double y, final double z, final double size, final SpriteSet sprites) {
        super(level, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F, sprites.first());
        this.sprites = sprites;
        this.lifetime = 4;
        float v = 0.08F * (this.random.nextFloat() - 0.5F); // -0.04..+0.04
        this.rCol = 0.55F + v;
        this.gCol = 0.10F + v * 0.5F;
        this.bCol = 0.12F + v * 0.5F;
        this.quadSize = 1.0F - (float)size * 0.5F;
        this.setSpriteFromAge(sprites);
    }

    public int getLightCoords(final float a) {
        return 15728880;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }
    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(final SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(
                final SimpleParticleType options,
                final ClientLevel level,
                final double x,
                final double y,
                final double z,
                final double xAux,
                final double yAux,
                final double zAux,
                final RandomSource random
        ) {
            return new ScytheSweepParticle(level, x, y, z, xAux, this.sprites);
        }
    }
}
