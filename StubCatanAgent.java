public class StubCatanAgent implements CatanAgent{ 

    public void init(int playerID){
        //do nothing, dont need to initialize for a stub
    }

    public Move chooseInitialSettlement(GameState state){
        //return the first legal option
        return state.getLegalMoves().get(0);
    }

    public Move chooseInitialRoad(GameState state){
        //return the first legal option
        return state.getLegalMoves().get(0);
    }

    public Move chooseMove(GameState state){
        //return the first legal option
        return state.getLegalMoves().get(0);
    }

    public Map<ResourceType, Integer> chooseDiscard(GameState state,int discardCount){
        //new empty map to return, since the stub doesn't need to actually discard, and is only meant to test gameplay and flow logic
        return new HashMap<>();
    } 

    public ResourceType chooseResource(GameState state){
        //return the first legal option for type of resource (ex: wood)
        return state.getLegalResourceChoices().get(0);
    }

    public int chooseRobberTarget(GameState state, List<Integer> possibleTargets){
        //return the first legal option for a player to rob
        return possibleTargets.get(0);
    } 

    public DevelopmentCard chooseDevelopmentCard(GameState state){
        //return the first legal option for a development card
        return state.getLegalDevelopmentCards().get(0);
    } 

}

 

 