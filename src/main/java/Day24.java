import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class Day24 {

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readFile(Function.identity());
        /*
        List<String> input = Arrays.asList(("Immune System:\n" +
                "17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2\n" +
                "989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3\n" +
                "\n" +
                "Infection:\n" +
                "801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1\n" +
                "4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4").split("\n"));
*/
        List<Group> immune = new ArrayList<>();
        int index = 1;
        for (; ; ++index) {
            String row = input.get(index);
            if (row.length() == 0) {
                break;
            }
            immune.add(createGroup(index, row));
        }
        List<Group> infection = new ArrayList<>();
        for (index += 2; index < input.size(); ++index) {
            infection.add(createGroup(index-2, input.get(index)));
        }
        int part1 = runFight(immune, infection, 0);
        System.out.println(part1);

        for (int i = 1; ; ++i) {
            int res = runFight(immune, infection, i);
            if (res > 0) {
                System.out.println(res);
                break;
            }
        }
    }

    private static int runFight(List<Group> immune1, List<Group> infection1, int deltaDamage) {
        List<Group> immune = new ArrayList<>(immune1);
        List<Group> infection = new ArrayList<>(infection1);

        List<Group> groups = new ArrayList<>(immune);
        groups.addAll(infection);
        groups.sort((g1, g2) -> g2.initiative - g1.initiative);
        for (Group group : groups) {
            group.reset();
        }
        for (Group group : immune) {
            group.damage += deltaDamage;
        }

        while (!immune.isEmpty() && !infection.isEmpty()) {
            immune.sort((g1, g2) -> g2.sortValue() - g1.sortValue());
            infection.sort((g1, g2) -> g2.sortValue() - g1.sortValue());
            Map<Integer, Integer> targets = new HashMap<>();
            for (int i = 0, j = 0; i < immune.size() || j < infection.size();) {
                if (i < immune.size()) {
                    if (j < infection.size()) {
                        Group g1 = immune.get(i);
                        Group g2 = infection.get(j);
                        if (g1.sortValue() > g2.sortValue()) {
                            addTarget(immune.get(i++), infection, targets);
                        } else {
                            addTarget(infection.get(j++), immune, targets);
                        }
                    } else {
                        addTarget(immune.get(i++), infection, targets);
                    }
                } else {
                    addTarget(infection.get(j++), immune, targets);
                }
            }
            for (Group group : groups) {
                Integer targetIndex = targets.get(group.index);
                if (targetIndex != null) {
                    Group other = getGroup(immune, targetIndex);
                    if (other == null) {
                        other = getGroup(infection, targetIndex);
                    }
                    if (other != null) {
                        int damage = group.calcDamage(other);
                        other.takeDamage(damage);
                        if (other.units == 0) {
                            immune.remove(other);
                            infection.remove(other);
                        }
                    }
                }
            }
        }
        for (Group group : immune) {
            group.damage -= deltaDamage;
        }
        int units = 0;
        for (Group group : immune) {
            units += group.units;
        }
        for (Group group : infection) {
            units -= group.units;
        }
        return units;
    }

    private static Group getGroup(List<Group> groups, int index) {
        for (Group group : groups) {
            if (group.index == index) {
                return group;
            }
        }
        return null;
    }

    private static void addTarget(Group group, List<Group> others, Map<Integer, Integer> targets) {
        Group best = null;
        int bestDamage = 0;
        for (Group other : others) {
            if (!targets.containsValue(other.index)) {
                int damage = group.calcDamage(other);
                if (damage == 0) {
                    continue;
                }
                if (damage > bestDamage
                        || (damage == bestDamage && other.power() > best.power())
                || (damage == bestDamage && other.power() == best.power() && other.initiative > best.initiative)) {
                    best = other;
                    bestDamage = damage;
                }
            }
        }
        if (best != null) {
            targets.put(group.index, best.index);
        }
    }

    private static Group createGroup(int index, String row) {
        String[] split = row.split(" ");
        int units = Integer.parseInt(split[0]);
        int hitPoints = Integer.parseInt(split[4]);
        Group group = new Group(index, units, hitPoints);
        int i = 5;
        for (; i < split.length; ++i) {
            if (split[i].startsWith("(")) {
                boolean immune = false;
                if (split[i].equals("(immune")) {
                    immune = true;
                }
                for (i += 2; ; ++i) {
                    String v = split[i];
                    String vv = v.substring(0, v.length()-1);
                    if (immune) {
                        group.immunes.add(vv);
                    } else {
                        group.weaknesses.add(vv);
                    }
                    if (v.endsWith(")")) {
                        break;
                    } else if (v.endsWith(";")) {
                        immune = !immune;
                        i += 2;
                    }
                }
                break;
            }
        }
        if (i == split.length) {
            i = 6;
        }
        group.damage = Integer.parseInt(split[i+6]);
        group.damageType = split[i+7];
        group.initiative = Integer.parseInt(split[split.length-1]);
        return group;
    }

    public static class Group {
        public int index, fullUnits, units, hitPoints, damage, initiative;
        public String damageType;
        public List<String> weaknesses, immunes;

        public Group(int index, int units, int hitPoints) {
            this.index = index;
            this.fullUnits = units;
            this.units = units;
            this.hitPoints = hitPoints;
            weaknesses = new ArrayList<>();
            immunes = new ArrayList<>();
        }

        public int sortValue() {
            return power()*100 + initiative;
        }

        public int calcDamage(Group other) {
            if (other.immunes.contains(damageType)) {
                return 0;
            }
            int d = units*damage;
            if (other.weaknesses.contains(damageType)) {
                d *= 2;
            }
            return d;
        }

        public int power() {
            return units*damage;
        }

        public void takeDamage(int damage) {
            units -= damage/hitPoints;
            if (units < 0) {
                units = 0;
            }
        }

        public void reset() {
            units = fullUnits;
        }

        @Override
        public String toString() {
            return "Group{" +
                    "units=" + units +
                    ", hitPoints=" + hitPoints +
                    ", damage=" + damage +
                    ", initiative=" + initiative +
                    ", damageType='" + damageType + '\'' +
                    ", weaknesses=" + weaknesses +
                    ", immunes=" + immunes +
                    '}';
        }
    }
}
