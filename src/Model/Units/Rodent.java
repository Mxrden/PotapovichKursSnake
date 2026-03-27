    package Model.Units;


    import Model.Effects.RodentEffect;

    public abstract class Rodent {
        protected final RodentEffect _effect;

        public abstract void onEaten(Snake snake);

        public Rodent(RodentEffect effect) {
            this._effect = effect;
        }

    }
