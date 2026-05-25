package com.example.nutriuniv.domain.product.specification;

import com.example.nutriuniv.domain.product.entity.Product;
import com.example.nutriuniv.domain.product.entity.ProductNutrient;
import com.example.nutriuniv.domain.product.enums.NutrientClaim;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class NutrientClaimSpecification {

    @SuppressWarnings("unchecked")
    private static Join<Product, ProductNutrient> getNutrientJoin(Root<Product> root) {
        return root.getJoins().stream()
                .filter(j -> "productNutrient".equals(j.getAttribute().getName()))
                .map(j -> (Join<Product, ProductNutrient>) j)
                .findFirst()
                .orElseGet(() -> root.join("productNutrient", JoinType.LEFT));
    }

    public static Specification<Product> hasClaims(List<NutrientClaim> claims) {
        if (claims == null || claims.isEmpty()) return (root, query, cb) -> null;

        Specification<Product> combined = (root, query, cb) -> null;
        for (NutrientClaim claim : claims) {
            combined = combined.and(forClaim(claim));
        }
        return combined;
    }

    public static Specification<Product> forClaim(NutrientClaim claim) {
        return switch (claim) {
            case LOW_CALORIE       -> lowCalorie();
            case NO_CALORIE        -> noCalorie();
            case LOW_SODIUM        -> lowSodium();
            case NO_SODIUM         -> noSodium();
            case LOW_SUGAR         -> lowSugar();
            case NO_SUGAR          -> noSugar();
            case LOW_FAT           -> lowFat();
            case NO_FAT            -> noFat();
            case LOW_TRANS_FAT     -> lowTransFat();
            case LOW_SATURATED_FAT -> lowSaturatedFat();
            case NO_SATURATED_FAT  -> noSaturatedFat();
            case LOW_CHOLESTEROL   -> lowCholesterol();
            case NO_CHOLESTEROL    -> noCholesterol();
            case FIBER_SOURCE      -> fiberSource();
            case HIGH_FIBER        -> highFiber();
            case PROTEIN_SOURCE    -> proteinSource();
            case HIGH_PROTEIN      -> highProtein();
        };
    }

    private static Specification<Product> lowCalorie() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            Predicate isLiquid = cb.like(cb.lower(n.get("servingSize")), "%ml%");
            Predicate isSolid  = cb.like(cb.lower(n.get("servingSize")), "%g%");
            return cb.or(
                    cb.and(isLiquid, cb.lessThan(n.get("caloriesPer100g"), new BigDecimal("20"))),
                    cb.and(isSolid,  cb.lessThan(n.get("caloriesPer100g"), new BigDecimal("40")))
            );
        };
    }

    private static Specification<Product> noCalorie() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            return cb.and(
                    cb.like(cb.lower(n.get("servingSize")), "%ml%"),
                    cb.lessThan(n.get("caloriesPer100g"), new BigDecimal("4"))
            );
        };
    }

    private static Specification<Product> lowSodium() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            return cb.and(
                    cb.or(cb.like(cb.lower(n.get("servingSize")), "%ml%"),
                            cb.like(cb.lower(n.get("servingSize")), "%g%")),
                    cb.lessThan(n.get("sodiumPer100g"), new BigDecimal("120"))
            );
        };
    }

    private static Specification<Product> noSodium() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            return cb.and(
                    cb.or(cb.like(cb.lower(n.get("servingSize")), "%ml%"),
                            cb.like(cb.lower(n.get("servingSize")), "%g%")),
                    cb.lessThan(n.get("sodiumPer100g"), new BigDecimal("5"))
            );
        };
    }

    private static Specification<Product> lowSugar() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            Predicate isLiquid = cb.like(cb.lower(n.get("servingSize")), "%ml%");
            Predicate isSolid  = cb.like(cb.lower(n.get("servingSize")), "%g%");
            return cb.or(
                    cb.and(isLiquid, cb.lessThan(n.get("sugarPer100g"), new BigDecimal("2.5"))),
                    cb.and(isSolid,  cb.lessThan(n.get("sugarPer100g"), new BigDecimal("5")))
            );
        };
    }

    private static Specification<Product> noSugar() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            return cb.and(
                    cb.or(cb.like(cb.lower(n.get("servingSize")), "%ml%"),
                            cb.like(cb.lower(n.get("servingSize")), "%g%")),
                    cb.lessThan(n.get("sugarPer100g"), new BigDecimal("0.5"))
            );
        };
    }

    private static Specification<Product> lowFat() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            Predicate isLiquid = cb.like(cb.lower(n.get("servingSize")), "%ml%");
            Predicate isSolid  = cb.like(cb.lower(n.get("servingSize")), "%g%");
            return cb.or(
                    cb.and(isLiquid, cb.lessThan(n.get("fatPer100g"), new BigDecimal("1.5"))),
                    cb.and(isSolid,  cb.lessThan(n.get("fatPer100g"), new BigDecimal("3")))
            );
        };
    }

    private static Specification<Product> noFat() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            return cb.and(
                    cb.or(cb.like(cb.lower(n.get("servingSize")), "%ml%"),
                            cb.like(cb.lower(n.get("servingSize")), "%g%")),
                    cb.lessThan(n.get("fatPer100g"), new BigDecimal("0.5"))
            );
        };
    }

    private static Specification<Product> lowTransFat() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            return cb.and(
                    cb.or(cb.like(cb.lower(n.get("servingSize")), "%ml%"),
                            cb.like(cb.lower(n.get("servingSize")), "%g%")),
                    cb.lessThan(n.get("transFatPer100g"), new BigDecimal("0.5"))
            );
        };
    }

    private static Specification<Product> lowSaturatedFat() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            Predicate isLiquid = cb.like(cb.lower(n.get("servingSize")), "%ml%");
            Predicate isSolid  = cb.like(cb.lower(n.get("servingSize")), "%g%");

            Predicate amountOk = cb.or(
                    cb.and(isLiquid, cb.lessThan(n.get("saturatedFatPer100g"), new BigDecimal("0.75"))),
                    cb.and(isSolid,  cb.lessThan(n.get("saturatedFatPer100g"), new BigDecimal("1.5")))
            );
            Predicate ratioOk = cb.lessThan(
                    cb.prod(n.<BigDecimal>get("saturatedFatPer100g"), new BigDecimal("9")),
                    cb.prod(n.<BigDecimal>get("caloriesPer100g"),     new BigDecimal("0.1"))
            );
            return cb.and(cb.or(isLiquid, isSolid), amountOk, ratioOk);
        };
    }

    private static Specification<Product> noSaturatedFat() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            Predicate isLiquid = cb.like(cb.lower(n.get("servingSize")), "%ml%");
            Predicate isSolid  = cb.like(cb.lower(n.get("servingSize")), "%g%");
            return cb.or(
                    cb.and(isLiquid, cb.lessThan(n.get("saturatedFatPer100g"), new BigDecimal("0.1"))),
                    cb.and(isSolid,  cb.lessThan(n.get("saturatedFatPer100g"), new BigDecimal("1.5")))
            );
        };
    }

    private static Specification<Product> lowCholesterol() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            Predicate isLiquid = cb.like(cb.lower(n.get("servingSize")), "%ml%");
            Predicate isSolid  = cb.like(cb.lower(n.get("servingSize")), "%g%");

            Predicate cholOk = cb.or(
                    cb.and(isLiquid, cb.lessThan(n.get("cholesterolPer100g"), new BigDecimal("10"))),
                    cb.and(isSolid,  cb.lessThan(n.get("cholesterolPer100g"), new BigDecimal("20")))
            );
            Predicate sfOk = cb.or(
                    cb.and(isLiquid, cb.lessThan(n.get("saturatedFatPer100g"), new BigDecimal("0.75"))),
                    cb.and(isSolid,  cb.lessThan(n.get("saturatedFatPer100g"), new BigDecimal("1.5")))
            );
            Predicate ratioOk = cb.lessThan(
                    cb.prod(n.<BigDecimal>get("saturatedFatPer100g"), new BigDecimal("9")),
                    cb.prod(n.<BigDecimal>get("caloriesPer100g"),     new BigDecimal("0.1"))
            );
            return cb.and(cb.or(isLiquid, isSolid), cholOk, sfOk, ratioOk);
        };
    }

    private static Specification<Product> noCholesterol() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            Predicate isLiquid = cb.like(cb.lower(n.get("servingSize")), "%ml%");
            Predicate isSolid  = cb.like(cb.lower(n.get("servingSize")), "%g%");

            Predicate cholOk = cb.lessThan(n.get("cholesterolPer100g"), new BigDecimal("5"));
            Predicate sfOk = cb.or(
                    cb.and(isLiquid, cb.lessThan(n.get("saturatedFatPer100g"), new BigDecimal("0.75"))),
                    cb.and(isSolid,  cb.lessThan(n.get("saturatedFatPer100g"), new BigDecimal("1.5")))
            );
            Predicate ratioOk = cb.lessThan(
                    cb.prod(n.<BigDecimal>get("saturatedFatPer100g"), new BigDecimal("9")),
                    cb.prod(n.<BigDecimal>get("caloriesPer100g"),     new BigDecimal("0.1"))
            );
            return cb.and(cb.or(isLiquid, isSolid), cholOk, sfOk, ratioOk);
        };
    }

    private static Specification<Product> fiberSource() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            Predicate hasUnit = cb.or(
                    cb.like(cb.lower(n.get("servingSize")), "%ml%"),
                    cb.like(cb.lower(n.get("servingSize")), "%g%")
            );
            Predicate per100gOk = cb.greaterThanOrEqualTo(n.get("fiberPer100g"), new BigDecimal("3"));
            Predicate per100kcalOk = cb.greaterThanOrEqualTo(
                    cb.prod(n.<BigDecimal>get("fiberPer100g"),    new BigDecimal("100")),
                    cb.prod(n.<BigDecimal>get("caloriesPer100g"), new BigDecimal("1.5"))
            );
            return cb.and(hasUnit, cb.or(per100gOk, per100kcalOk));
        };
    }

    private static Specification<Product> highFiber() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            Predicate hasUnit = cb.or(
                    cb.like(cb.lower(n.get("servingSize")), "%ml%"),
                    cb.like(cb.lower(n.get("servingSize")), "%g%")
            );
            Predicate per100gOk = cb.greaterThanOrEqualTo(n.get("fiberPer100g"), new BigDecimal("6"));
            Predicate per100kcalOk = cb.greaterThanOrEqualTo(
                    cb.prod(n.<BigDecimal>get("fiberPer100g"),    new BigDecimal("100")),
                    cb.prod(n.<BigDecimal>get("caloriesPer100g"), new BigDecimal("3"))
            );
            return cb.and(hasUnit, cb.or(per100gOk, per100kcalOk));
        };
    }

    private static Specification<Product> proteinSource() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            Predicate isLiquid = cb.like(cb.lower(n.get("servingSize")), "%ml%");
            Predicate isSolid  = cb.like(cb.lower(n.get("servingSize")), "%g%");

            Predicate solidOk  = cb.and(isSolid,
                    cb.greaterThanOrEqualTo(n.get("proteinPer100g"), new BigDecimal("5.5")));
            Predicate liquidOk = cb.and(isLiquid,
                    cb.greaterThanOrEqualTo(n.get("proteinPer100g"), new BigDecimal("2.75")));
            Predicate per100kcalOk = cb.and(
                    cb.or(isLiquid, isSolid),
                    cb.greaterThanOrEqualTo(
                            cb.prod(n.<BigDecimal>get("proteinPer100g"),  new BigDecimal("100")),
                            cb.prod(n.<BigDecimal>get("caloriesPer100g"), new BigDecimal("2.75"))
                    )
            );
            return cb.or(solidOk, liquidOk, per100kcalOk);
        };
    }

    private static Specification<Product> highProtein() {
        return (root, query, cb) -> {
            Join<Product, ProductNutrient> n = getNutrientJoin(root);
            Predicate isLiquid = cb.like(cb.lower(n.get("servingSize")), "%ml%");
            Predicate isSolid  = cb.like(cb.lower(n.get("servingSize")), "%g%");

            Predicate solidOk  = cb.and(isSolid,
                    cb.greaterThanOrEqualTo(n.get("proteinPer100g"), new BigDecimal("11")));
            Predicate liquidOk = cb.and(isLiquid,
                    cb.greaterThanOrEqualTo(n.get("proteinPer100g"), new BigDecimal("5.5")));
            Predicate per100kcalOk = cb.and(
                    cb.or(isLiquid, isSolid),
                    cb.greaterThanOrEqualTo(
                            cb.prod(n.<BigDecimal>get("proteinPer100g"),  new BigDecimal("100")),
                            cb.prod(n.<BigDecimal>get("caloriesPer100g"), new BigDecimal("5.5"))
                    )
            );
            return cb.or(solidOk, liquidOk, per100kcalOk);
        };
    }
}