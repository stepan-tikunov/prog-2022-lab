package edu.ifmo.tikunov.lab5.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import edu.ifmo.tikunov.lab5.common.CreationDateSpecifiable;
import edu.ifmo.tikunov.lab5.common.Identifiable;
import edu.ifmo.tikunov.lab5.common.composite.Composite;
import edu.ifmo.tikunov.lab5.common.composite.CompositeConstructor;
import edu.ifmo.tikunov.lab5.common.composite.CompositeParser;
import edu.ifmo.tikunov.lab5.common.validate.Constraint;
import edu.ifmo.tikunov.lab5.common.validate.ConstraintType;
import edu.ifmo.tikunov.lab5.common.validate.Constraints;
import edu.ifmo.tikunov.lab5.common.validate.Description;
import edu.ifmo.tikunov.lab5.common.validate.FieldName;

@Composite
public class SpaceMarine implements Comparable<SpaceMarine>, Identifiable<Long>, CreationDateSpecifiable, Serializable {

	// region field declarations

	@FieldName("id")
	@Constraints({
		@Constraint(type = ConstraintType.NOT_EQUAL_TO, value = "null"),
		@Constraint(type = ConstraintType.MORE_THAN, value = "0")
	})
	private Long id; // !null, >0, unique, auto

	@FieldName("name")
	@Constraints({
		@Constraint(type = ConstraintType.NOT_EQUAL_TO, value = ""),
		@Constraint(type = ConstraintType.NOT_EQUAL_TO, value = " ")
	})
	@Description("name")
	private String name; // !null, !""

	@FieldName("coordinates")
	@Constraint(type = ConstraintType.NOT_EQUAL_TO, value = "null")
	private Coordinates coordinates; // !null

	@FieldName("creation_date")
	@Constraint(type = ConstraintType.NOT_EQUAL_TO, value = "null")
	private LocalDateTime creationDate; // !null, auto

	@FieldName("health")
	@Constraints({
		@Constraint(type = ConstraintType.NOT_EQUAL_TO, value = "null"),
		@Constraint(type = ConstraintType.MORE_THAN, value = "0")
	})
	@Description("health")
	private Double health; // !null, >0

	@FieldName("category")
	@Constraint(type = ConstraintType.NOT_EQUAL_TO, value = "null")
	@Description("category")
	private AstartesCategory category; // !null

	@FieldName("weapon_type")
	@Description("weapon type")
	private Weapon weaponType; // nullable

	@FieldName("melee_weapon")
	@Description("melee weapon")
	private MeleeWeapon meleeWeapon; // nullable

	@FieldName("chapter")
	private Chapter chapter; // nullable

	// endregion

	// region setters

	@Override
	@JsonSetter("id")
	public void setId(Long id) {
		this.id = id;
	}

	@JsonSetter("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonSetter("coordinates")
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	@JsonSetter("creation_date")
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	@JsonSetter("health")
	public void setHealth(Double health) {
		this.health = health;
	}

	@JsonSetter("category")
	public void setCategory(AstartesCategory category) {
		this.category = category;
	}

	@JsonSetter("weapon_type")
	public void setWeaponType(Weapon weaponType) {
		this.weaponType = weaponType;
	}

	@JsonSetter("melee_weapon")
	public void setMeleeWeapon(MeleeWeapon meleeWeapon) {
		this.meleeWeapon = meleeWeapon;
	}

	@JsonSetter("chapter")
	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	// endregion

	// region getters

	@Override
	@JsonGetter("id")
	public Long getId() {
		return id;
	}

	@JsonGetter("name")
	public String getName() {
		return name;
	}

	@JsonGetter("coordinates")
	public Coordinates getCoordinates() {
		return coordinates;
	}

	@Override
	@JsonGetter("creation_date")
	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	@JsonGetter("health")
	public Double getHealth() {
		return health;
	}

	@JsonGetter("category")
	public AstartesCategory getCategory() {
		return category;
	}

	@JsonGetter("weapon_type")
	public Weapon getWeaponType() {
		return weaponType;
	}

	@JsonGetter("melee_weapon")
	public MeleeWeapon getMeleeWeapon() {
		return meleeWeapon;
	}
	@JsonGetter("chapter")
	public Chapter getChapter() {
		return chapter;
	}

	// endregion

	@Override
	public int compareTo(SpaceMarine o) {
		return (int) (health - o.health);
	}

	@Override
	public boolean idIsSet() {
		return id != null;
	}

	@Override
	public String toString() {
		return CompositeParser.stringValue(this);
	}

	public SpaceMarine() {
	}

	@CompositeConstructor
	public SpaceMarine(
		@FieldName("name") String name,
		@FieldName("coordinates") Coordinates coordinates,
		@FieldName("health") Double health,
		@FieldName("category") AstartesCategory category,
		@FieldName("weapon_type") Weapon weaponType,
		@FieldName("melee_weapon") MeleeWeapon meleeWeapon,
		@FieldName("chapter") Chapter chapter
	) {
		this.name = name;
		this.coordinates = coordinates;
		this.health = health;
		this.category = category;
		this.weaponType = weaponType;
		this.meleeWeapon = meleeWeapon;
		this.chapter = chapter;
		this.creationDate = LocalDateTime.now();
	}
}
