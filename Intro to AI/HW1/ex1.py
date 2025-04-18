import search
from itertools import product
from itertools import permutations

ids = ["207036211", "319000725"]


class HarryPotterProblem(search.Problem):
    """This class implements a medical problem according to problem description file"""

    def __init__(self, initial):
        self.m = len(initial['map'])
        self.death_eater_path = []  # describe the path the death eater do (A,B,C, B, A)
        self.death_eater_path_len = []  # describe the lenght of the path the death eater do
        self.death_eater_position = []  # describe the position the death eater do (x,y)
        self.death_eater_count = []  # describe the counter-index the death eater at

        for path in initial['death_eaters'].values():
            path_len = len(path + path[::-1]) - 1
            self.death_eater_path.append(path[:len(path) - 1] + path[::-1])
            self.death_eater_position.append(path[0])
            self.death_eater_path_len.append(path_len)
            self.death_eater_count.append(0)

        for row in range(len(initial["map"])):
            for col in range(len(initial["map"][0])):
                if initial["map"][row][col] == 'V':
                    self.voldemort_position = row, col
                    break
        # מספר העמודות (אורך כל רשימה בתוך המפה, נניח שכולם באותו אורך)
        self.n = len(initial['map'][0])
        self.map = (initial['map'])
        initial_state = self.convert_state_to_tuple(initial)
        search.Problem.__init__(self, initial_state)

    def convert_state_to_tuple(self, state):
        # המרת המפה לטאפלים של טאפלים
        map_tuple = tuple(tuple(row) for row in state['map'])

        # המרת הקוסמים לטאפלים
        wizards_tuple = tuple(
            (name, position, property_value)
            for name, (position, property_value) in state['wizards'].items()
        )

        # המרת אוכלי המוות לטאפלים
        death_eaters_tuple = tuple(
            (name, tuple(positions))  # המרת הרשימה של המיקומים לטאפלים
            for name, positions in state['death_eaters'].items()
        )
        death_eaters_state = (tuple(self.death_eater_position), tuple(self.death_eater_count))
        # המרת ההורקרוקסים לטאפלים
        horcruxes_tuple = tuple(state['horcruxes'])

        voldemort_state = True

        # החזרת המצב כטאפלים של טאפלים
        return map_tuple, wizards_tuple, death_eaters_tuple, horcruxes_tuple, voldemort_state, death_eaters_state

    def possible_actions(self, wizard, horcruxes, gmap, n, m):
        actions = []
        current_place = wizard[1]
        wName = wizard[0]
        numCol = n - 1
        numRow = m - 1
        x = current_place[0]
        y = current_place[1]
        actions.append(("wait", wName))  # אפשרות לחכות

        # בדיקה של כל הפעולות האפשריות
        if x + 1 <= numRow and gmap[x + 1][y] != 'I':  # תזוזה למטה
            actions.append(("move", wName, (x + 1, y)))
        if 0 <= x - 1 and gmap[x - 1][y] != 'I':  # תזוזה למעלה
            actions.append(("move", wName, (x - 1, y)))
        if 0 <= y - 1 and gmap[x][y - 1] != 'I':  # תזוזה שמאלה
            actions.append(("move", wName, (x, y - 1)))
        if y + 1 <= numCol and gmap[x][y + 1] != 'I':  # תזוזה ימינה
            actions.append(("move", wName, (x, y + 1)))

        # בדיקה אם אפשר להרוס הוקרוקס
        for horcrux in horcruxes:
            if horcrux == (-1, -1):
                continue
            if (x, y) == horcrux:
                index = self.find_horcruxes_index(horcruxes, horcrux)
                actions.append(("destroy", wName, index))

        # בדיקה אם אפשר להרוג את וולדמורט
        if gmap[x][y] == 'V' and all(h == (-1, -1) for h in horcruxes) and wName == 'Harry Potter':
            actions.append(("kill", 'Harry Potter'))

        return actions

    def actions(self, state):
        """Return the valid actions that can be executed in the given state."""


        all_wizards_actions = []
        # בדיקה אם אחד מהקוסמים הגיע למספר חיים 0
        for wizard in state[1]:  # state[1] מכיל את כל הקוסמים
            _, _, lives = wizard
            if lives <= 0:
                return []  # אין פעולות אפשריות, עצור את החיפוש

        for wizard in state[1]:  # לכל קוסם, חשב את כל הפעולות האפשריות
            possible = self.possible_actions(wizard, state[3], self.map, self.n, self.m)
            all_wizards_actions.append(possible)


        # ייצור כל הקומבינציות האפשריות של הפעולות
        all_combinations = product(*all_wizards_actions)
        return all_combinations

    def update_wizard_position(self, state, wizard_name, new_position, new_life):
        """update the wizard position and life"""
        gmap, wizards, death_eaters, horcruxes, voldemort_state, death_eaters_state = state

        # חיפוש הקוסם בעזרת יצירת עותק חדש של רשימת הקוסמים
        updated_wizards = []

        for wizard in wizards:
            name, _, _ = wizard
            if name == wizard_name:
                # עדכון המיקום של הקוסם
                updated_wizards.append((name, new_position, new_life))
            else:
                # שמירה על שאר הקוסמים ללא שינוי
                updated_wizards.append(wizard)
        # יצירת מצב חדש עם טאפלים מעודכנים
        new_state = (
            tuple(gmap),
            tuple(updated_wizards),  # המרת רשימת הקוסמים לטאפלים
            tuple(death_eaters),
            tuple(horcruxes),
            voldemort_state,
            death_eaters_state
        )
        return new_state

    def check_deathEater(self, wizard_position, state):
        gmap, wizards, death_eaters, horcruxes, voldemort_state, death_eaters_state = state
        death_eaters_position, _ = death_eaters_state
        num_death_eaters = len(self.death_eater_path_len)
        counter = 0 #we return how many deatEaters are in same place as the wizard
        for i in range(num_death_eaters):
            if death_eaters_position[i] == wizard_position:
               counter += 1
        return counter

    def move_deathEater(self, state):
        gmap, wizards, death_eaters, horcruxes, voldemort_state, death_eaters_state = state
        death_eaters_position, death_eaters_count = death_eaters_state
        death_eaters_position_list = list(death_eaters_position)
        death_eaters_count_list = list(death_eaters_count)
        num_death_eaters = len(death_eaters_position_list)
        for i in range(num_death_eaters):
            death_eaters_count_list[i] += 1
            if death_eaters_count_list[i] >= self.death_eater_path_len[i]:
                death_eaters_count_list[i] = 1
            death_eaters_position_list[i] = self.death_eater_path[i][death_eaters_count_list[i]]
        death_eaters_position = (tuple(death_eaters_position_list), tuple(death_eaters_count_list))
        new_state = (
            gmap,
            wizards,
            death_eaters,
            horcruxes,
            voldemort_state,
            death_eaters_position
        )
        return new_state

    def get_wizard_life(self, state, wizard_name):
        _, wizards, _, _, _, _ = state  # פירוק המצב
        # חיפוש הקוסם ברשימת הקוסמים
        for wizard in wizards:
            name, _, life = wizard
            if name == wizard_name:
                return life

    def get_wizard_position(self, state, wizard_name):
        """
        מחזירה את המיקום הנוכחי של קוסם לפי שמו.

        Args:
            state (tuple): המצב הנוכחי (כטאפל של טאפלים).
            wizard_name (str): שם הקוסם לחיפוש.

        Returns:
            tuple: המיקום של הקוסם (x, y).

        Raises:
            ValueError: אם הקוסם לא נמצא.
        """
        _, wizards, _, _, _, _ = state  # פירוק המצב

        # חיפוש הקוסם ברשימת הקוסמים
        for wizard in wizards:
            name, position, _ = wizard
            if name == wizard_name:
                return position

    def find_horcruxes_index(self, horcruxes, destroyed_horcrux):
        counter = 0
        for horcrux in horcruxes:
            if horcrux == (-1, -1):
                counter += 1
                continue
            if horcrux == destroyed_horcrux:
                return counter
            counter += 1
        return -1

    def destroy_horcruxes(self, state, destroyed_horcrux_index):
        """
        מעדכנת את רשימת ההורקרוקסים על ידי הסרת הורקרוקס שהושמד.

        Args:
            state (tuple): המצב הנוכחי כטאפל של טאפלים.
            destroyed_horcrux_index (int): האינדקס .

        Returns:
            tuple: מצב חדש עם רשימת הורקרוקסים מעודכנת.

        Raises:
            ValueError: אם ההורקרוקס שהושמד אינו נמצא ברשימה.
        """
        game_map, wizards, death_eaters, horcruxes, voldemort_state, death_eaters_state = state  # פירוק המצב

        if destroyed_horcrux_index == -1:
            return state
        update_horcruxes = list(horcruxes)
        if update_horcruxes[destroyed_horcrux_index] == (-1, -1):
            return state
        update_horcruxes[destroyed_horcrux_index] = (-1, -1)
        update_horcruxes = tuple(update_horcruxes)
        return game_map, wizards, death_eaters, update_horcruxes, voldemort_state, death_eaters_state

    def kill_Voldemort(self, state):
        game_map, wizards, death_eaters, horcruxes, voldemort_state, death_eaters_state = state
        updated_voldemort_state = False
        return game_map, wizards, death_eaters, horcruxes, updated_voldemort_state, death_eaters_state

    def result(self, state,
               action):
        """Return the state that results from executing the given action in the given state."""
        # if kill action made, need to change voldemort_state to "False"
        new_state = self.move_deathEater(state)  # update deathEater position
        for act in action:
            nWizard = act[1]  # the wizard name in order to know how much life he has and if he is Harry
            life = self.get_wizard_life(new_state, nWizard)
            currPosition = self.get_wizard_position(new_state, nWizard)
            vol_Position = self.voldemort_position
            if act[0] == 'move':
                new_position = act[2]
                # wizard we moved
                life = life - self.check_deathEater(new_position, new_state)  # check if a death eater in the same place as the and lower the wizard life by one if needed
                if new_position == vol_Position and nWizard != "Harry Potter":
                    life = 0  # the wizard met voldemort and died
                elif new_position == vol_Position and nWizard == "Harry Potter" and any(h != (-1, -1) for h in
                                                                                        state[
                                                                                            3]):
                    life = 0  # Harry met voldemort and there were still horcruxes
                new_state = self.update_wizard_position(new_state, nWizard, new_position, life)  # update the state
            elif act[0] == 'kill':
                new_state = self.kill_Voldemort(new_state)  # We succeeded to kill voldemort
            elif act[0] == 'destroy':
                life = life - self.check_deathEater(currPosition, new_state)
                new_state = self.destroy_horcruxes(new_state, act[2])#update the horcruxes
                new_state = self.update_wizard_position(new_state, nWizard, currPosition, life)#update the life of the wizard if death eater was in the same tile with the horcruxe
            else:  # if the action is wait then we have to check if a death eater in the same place as the wizard
                # if the action is wait then we have to check if a death eater in the same place as the wizard
                life = life - self.check_deathEater(currPosition, new_state)  # lower the wizard life by the number of death eaters in the same tile with him
                new_state = self.update_wizard_position(new_state, nWizard, currPosition, life)
        return new_state

    def goal_test(self, state):
        voldemort = state[4]
        horcruxes = state[3]
        for wizard in state[1]:
            if wizard[2] <= 0:
                return False
        if any(h != (-1, -1) for h in horcruxes) or voldemort:
            return False
        return True

    def manhattan_distance(self, pos1, pos2):
        return abs(pos1[0] - pos2[0]) + abs(pos1[1] - pos2[1])

    def h(self, node):
        state = node.state
        wizards = state[1]  # מיקומי הקוסמים ומספר החיים שנותרו להם
        horcruxes = [h for h in state[3] if h != (-1, -1)]  # הורקרוקסים שטרם הושמדו
        voldemort_position = self.voldemort_position  # מיקומו הנוכחי של וולדמורט
        voldemort = state[4]  # האם וולדמורט חי

        #if one of the wizards died
        for _, _, lives in wizards:
            if lives <= 0:
                return float('inf')

        # אם וולדמורט כבר מת, עלות היא 0
        if not voldemort:
            return 0

        heuristic = 0

        # מציאת מיקום הארי
        harry_position = None
        for wizard in wizards:
            if wizard[0] == "Harry Potter":
                harry_position = wizard[1]
                break

        # אם יש רק קוסם אחד
        if len(wizards) == 1:
            # אם אין עוד הורקרוקסים, מחושב רק המרחק לוולדמורט
            if not horcruxes:
                return self.manhattan_distance(harry_position, voldemort_position) + 1  # +1 עבור תור להרוג את וולדמורט

            # חישוב כל המסלולים האפשריים דרך ההורקרוקסים
            min_total_distance = float('inf')

            for perm in permutations(horcruxes):
                current_position = harry_position
                total_distance = 0

                for horcrux in perm:
                    total_distance += self.manhattan_distance(current_position, horcrux) + 1  # +1 עבור תור להרוס
                    current_position = horcrux

                # הוספת המרחק מוולדמורט לאחר המסלול
                total_distance += self.manhattan_distance(current_position, voldemort_position) + 1  # +1 עבור תור להרוג
                min_total_distance = min(min_total_distance, total_distance)

            return min_total_distance

        # 1. חישוב המרחק המינימלי לכל הורקרוקס
        for horcrux in horcruxes:
            if horcrux == (-1, -1):
                continue
            min_distance = float('inf')
            for wizard in wizards:
                _, wizard_position, lives = wizard  # פירוק הטופל של הקוסם
                min_distance = min(min_distance, self.manhattan_distance(wizard_position, horcrux))
            heuristic += min_distance + 1
        voldemort_position = self.voldemort_position
        remaining_horcruxes = sum(1 for horcrux in horcruxes if horcrux != (-1, -1))
        for wizard in wizards:
            wizard_name, wizard_position, _ = wizard
            if wizard_name == 'Harry Potter':
                heuristic += (self.manhattan_distance(wizard_position,
                                                      voldemort_position) / (
                                          remaining_horcruxes + 1)) + 1  # another turn in order to kill Voldemort
                break
        return heuristic


def create_harrypotter_problem(game):
    return HarryPotterProblem(game)