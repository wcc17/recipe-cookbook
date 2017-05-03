drop table RecipeIngredient cascade constraints;
drop table RecipeMeal cascade constraints;
drop table Recipe;
drop table Ingredient;
drop table Meal;

drop sequence meal_seq;

create table Recipe (
    name varchar2(15) primary key,
    instructions varchar2(4000),
    category varchar2(30)
);

create table Ingredient (
    name varchar2(15) primary key,
    inFridge char(1),
    calories number(4),
    fat number(3),
    protein number(3),
    sugar number(3),
    sodium number(3)
);
alter table Ingredient add (constraint inFridgeCK check(inFridge in('Y', 'y', 'N', 'n')));

create table Meal (
    id number(10) primary key,
    mealType varchar2(20),
    dayOfWeek varchar2(15),
    weekStart date --THIS SHOULD ALWAYS BE A SUNDAY
);
alter table Meal add (constraint mealType check(mealType in ('breakfast', 'lunch', 'dinner')));
alter table Meal add (constraint dayOfWeek check(dayOfWeek in ('monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday')));

create sequence meal_seq START WITH 1;

create table RecipeIngredient(
    recipeName varchar2(15),
    ingredientName varchar2(15),
    constraint fk_RecipeIng_recipeName foreign key (recipeName) references Recipe(name) on delete cascade,
    constraint fk_RecipeIng_ingredientName foreign key (ingredientName) references Ingredient(name) on delete cascade,
    primary key (recipeName, ingredientName)
);

create table RecipeMeal(
    recipeName varchar2(15),
    mealId number(10),
    constraint fk_RecipeMeal_recipeName foreign key (recipeName) references Recipe(name) on delete cascade,
    constraint fk_RecipeMeal_mealId foreign key (mealId) references Meal(id) on delete cascade,
    primary key(recipeName, mealId)
);

create or replace trigger meal_auto_inc
before insert on Meal
for each row
begin
    select meal_seq.NEXTVAL
    into :new.id
    from dual;
end;
/

insert into Recipe values('Pizza', '1. Get pizza ingredients' || CHR(10) || '2. Make pizza', 'Junk Food');
insert into Recipe values('Spaghetti', '1. Get spaghetti ingredients' || CHR(10) || '2. Heat up sauce' || CHR(10) || '3. Make spaghetti', 'Pasta');
insert into Recipe values('Salad', '1. Get a bunch of salad stuff' || CHR(10) || '2. Mix up salad stuff' || CHR(10) || '3. Eat salad', 'Vegetable');

insert into Ingredient values('Pepperoni', 'Y', 30, 25, 0, 0, 5);
insert into Ingredient values('Marinara', 'Y', 25, 15, 1, 3, 5);
insert into Ingredient values('Noodles', 'N', 20, 20, 1, 2, 5);
insert into Ingredient values('Lettuce', 'N', 5, 5, 0, 0, 5);
insert into Ingredient values('Dressing', 'Y', 15, 15, 0, 15, 5);

insert into Meal(mealType, dayOfWeek, weekStart) values('dinner', 'monday', to_date('04/30/2017', 'MM/DD/YYYY'));
insert into Meal(mealType, dayOfWeek, weekStart) values('lunch', 'tuesday', to_date('04/30/2017', 'MM/DD/YYYY'));
insert into Meal(mealType, dayOfWeek, weekStart) values('dinner', 'wednesday', to_date('04/30/2017', 'MM/DD/YYYY'));

insert into RecipeIngredient values('Pizza', 'Pepperoni');
insert into RecipeIngredient values('Pizza', 'Marinara');
insert into RecipeIngredient values('Spaghetti', 'Marinara');
insert into RecipeIngredient values('Spaghetti', 'Noodles');
insert into RecipeIngredient values('Salad', 'Lettuce');
insert into RecipeIngredient values('Salad', 'Dressing');

insert into RecipeMeal values('Pizza', 1);
insert into RecipeMeal values('Spaghetti', 2);
insert into RecipeMeal values('Salad', 3);

commit;

--select * from Recipe;
--select * from Ingredient;
--select * from Meal;
--select * from RecipeIngredient;
--select * from RecipeMeal;