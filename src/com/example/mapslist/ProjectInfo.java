package com.example.mapslist;

public class ProjectInfo {
        String addressLine1;
        String addressLine2;
        String brochure;
        String city;
        String description;
        Documents[] imageUrls;
        boolean hidePrice;
        String landmark;
        String listingId;
        String listingName;
        String locality;
        String maxArea;
        String minArea;
        String maxPrice;
        String minPrice;
        String maxPricePerSqft;
        String minPricePerSqft;
        String noOfAvailableUnits;
        String noOfBlocks;
        String noOfUnits;
        String otherInfo;
        String packageId;
        String posessionDate;
        String projectType;
        String status;
        Summary summary[];
        String url;
        String[] videoLinks;
        String[]amenities;
        String approvalNumber;
        String approvedBy;
        String bankApprovals;
        String builderCredaiStatus;
        String builderDescription;
        String builderId;
        String builderLogo;
        String builderName;
        String builderUrl;
        String electricityConnection;
        String lastMileLandmark;
        String lastMileLat;
        String lastMileLon;
        String propertyTypes;
        String lat;
        String lon;
        String otherAmenities;
        String otherBanks;
        String specification;
        String waterTypes;

        class Summary{
            String area;
            String bathrooms;
            String bedroom;
            String carParking;
            String[]floorPlans;
            String landArea;
            String noOfUnits;
            String price;
            String propertyType;
        }
        class Documents{
            String directionFacing;
            boolean primary;
            String reference;
            String text;
            String type;
        }
    }


