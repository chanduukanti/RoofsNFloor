package com.roofnfloors.parser;

public class Project {
    private final String mProjectid;
    private String mProjectName;
    private Double mLatitude;
    private Double mLongitude;
    private ProjectDetails projectDetails;

    public Project(String id) {
        mProjectid = id;
    }

    public class ProjectDetails {
        private String addressLine1;
        private String addressLine2;
        private String brochure;
        private String city;
        private String description;
        private Documents[] documents;
        private boolean hidePrice;
        private String landmark;
        private String listingId;
        private String listingName;
        private String locality;
        private String maxArea;
        private String minArea;
        private String maxPrice;
        private String minPrice;
        private String maxPricePerSqft;
        private String minPricePerSqft;
        private String noOfAvailableUnits;
        private String noOfBlocks;
        private String noOfUnits;
        private String otherInfo;
        private String packageId;
        private String posessionDate;
        private String projectType;
        private String status;
        private Summary summary[];
        private String url;
        private String[] videoLinks;
        private String amenities;
        private String approvalNumber;
        private String approvedBy;
        private String bankApprovals;
        private String builderCredaiStatus;
        private String builderDescription;
        private String builderId;
        private String builderLogo;
        private String builderName;
        private String builderUrl;
        private String electricityConnection;
        private String lastMileLandmark;
        private String lastMileLat;
        private String lastMileLon;
        private String propertyTypes;
        private String lat;
        private String lon;
        private String otherAmenities;
        private String otherBanks;
        private String specification;
        private String waterTypes;
        private String ImageUrls;

        public class Summary {
            public String area;
            public String bathrooms;
            public String bedroom;
            public String carParking;
            public String[] floorPlans;
            public String landArea;
            public String noOfUnits;
            public String price;
            public String propertyType;
        }

        public class Documents {
            public String directionFacing;
            public boolean primary;
            public String reference;
            public String text;
            public String type;

            @Override
            public String toString() {
                return reference;
            }
        }

        public ProjectDetails() {
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public String getBrochure() {
            return brochure;
        }

        public void setBrochure(String brochure) {
            this.brochure = brochure;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Documents[] getDocuments() {
            return documents;
        }

        public void setDocuments(Documents[] documents) {
            this.documents = documents;
        }

        public boolean isHidePrice() {
            return hidePrice;
        }

        public void setHidePrice(boolean hidePrice) {
            this.hidePrice = hidePrice;
        }

        public String getLandmark() {
            return landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

        public String getListingId() {
            return listingId;
        }

        public void setListingId(String listingId) {
            this.listingId = listingId;
        }

        public String getListingName() {
            return listingName;
        }

        public void setListingName(String listingName) {
            this.listingName = listingName;
        }

        public String getLocality() {
            return locality;
        }

        public void setLocality(String locality) {
            this.locality = locality;
        }

        public String getMaxArea() {
            return maxArea;
        }

        public void setMaxArea(String maxArea) {
            this.maxArea = maxArea;
        }

        public String getMinArea() {
            return minArea;
        }

        public void setMinArea(String minArea) {
            this.minArea = minArea;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(String minPrice) {
            this.minPrice = minPrice;
        }

        public String getMaxPricePerSqft() {
            return maxPricePerSqft;
        }

        public void setMaxPricePerSqft(String maxPricePerSqft) {
            this.maxPricePerSqft = maxPricePerSqft;
        }

        public String getMinPricePerSqft() {
            return minPricePerSqft;
        }

        public void setMinPricePerSqft(String minPricePerSqft) {
            this.minPricePerSqft = minPricePerSqft;
        }

        public String getNoOfAvailableUnits() {
            return noOfAvailableUnits;
        }

        public void setNoOfAvailableUnits(String noOfAvailableUnits) {
            this.noOfAvailableUnits = noOfAvailableUnits;
        }

        public String getNoOfBlocks() {
            return noOfBlocks;
        }

        public void setNoOfBlocks(String noOfBlocks) {
            this.noOfBlocks = noOfBlocks;
        }

        public String getNoOfUnits() {
            return noOfUnits;
        }

        public void setNoOfUnits(String noOfUnits) {
            this.noOfUnits = noOfUnits;
        }

        public String getOtherInfo() {
            return otherInfo;
        }

        public void setOtherInfo(String otherInfo) {
            this.otherInfo = otherInfo;
        }

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public String getPosessionDate() {
            return posessionDate;
        }

        public void setPosessionDate(String posessionDate) {
            this.posessionDate = posessionDate;
        }

        public String getProjectType() {
            return projectType;
        }

        public void setProjectType(String projectType) {
            this.projectType = projectType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Summary[] getSummary() {
            return summary;
        }

        public void setSummary(Summary[] summary) {
            this.summary = summary;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String[] getVideoLinks() {
            return videoLinks;
        }

        public void setVideoLinks(String[] videoLinks) {
            this.videoLinks = videoLinks;
        }

        public String getAmenities() {
            return amenities;
        }

        public void setAmenities(String amenities) {
            this.amenities = amenities;
        }

        public String getApprovalNumber() {
            return approvalNumber;
        }

        public void setApprovalNumber(String approvalNumber) {
            this.approvalNumber = approvalNumber;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }

        public String getBankApprovals() {
            return bankApprovals;
        }

        public void setBankApprovals(String bankApprovals) {
            this.bankApprovals = bankApprovals;
        }

        public String getBuilderCredaiStatus() {
            return builderCredaiStatus;
        }

        public void setBuilderCredaiStatus(String builderCredaiStatus) {
            this.builderCredaiStatus = builderCredaiStatus;
        }

        public String getBuilderDescription() {
            return builderDescription;
        }

        public void setBuilderDescription(String builderDescription) {
            this.builderDescription = builderDescription;
        }

        public String getBuilderId() {
            return builderId;
        }

        public void setBuilderId(String builderId) {
            this.builderId = builderId;
        }

        public String getBuilderLogo() {
            return builderLogo;
        }

        public void setBuilderLogo(String builderLogo) {
            this.builderLogo = builderLogo;
        }

        public String getBuilderName() {
            return builderName;
        }

        public void setBuilderName(String builderName) {
            this.builderName = builderName;
        }

        public String getBuilderUrl() {
            return builderUrl;
        }

        public void setBuilderUrl(String builderUrl) {
            this.builderUrl = builderUrl;
        }

        public String getElectricityConnection() {
            return electricityConnection;
        }

        public void setElectricityConnection(String electricityConnection) {
            this.electricityConnection = electricityConnection;
        }

        public String getLastMileLandmark() {
            return lastMileLandmark;
        }

        public void setLastMileLandmark(String lastMileLandmark) {
            this.lastMileLandmark = lastMileLandmark;
        }

        public String getLastMileLat() {
            return lastMileLat;
        }

        public void setLastMileLat(String lastMileLat) {
            this.lastMileLat = lastMileLat;
        }

        public String getLastMileLon() {
            return lastMileLon;
        }

        public void setLastMileLon(String lastMileLon) {
            this.lastMileLon = lastMileLon;
        }

        public String getPropertyTypes() {
            return propertyTypes;
        }

        public void setPropertyTypes(String propertyTypes) {
            this.propertyTypes = propertyTypes;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getOtherAmenities() {
            return otherAmenities;
        }

        public void setOtherAmenities(String otherAmenities) {
            this.otherAmenities = otherAmenities;
        }

        public String getOtherBanks() {
            return otherBanks;
        }

        public void setOtherBanks(String otherBanks) {
            this.otherBanks = otherBanks;
        }

        public String getSpecification() {
            return specification;
        }

        public void setSpecification(String specification) {
            this.specification = specification;
        }

        public String getWaterTypes() {
            return waterTypes;
        }

        public void setWaterTypes(String waterTypes) {
            this.waterTypes = waterTypes;
        }

        public String getImageUrls() {
            return ImageUrls;
        }

        public void setImageUrls(String imageUrls) {
            ImageUrls = imageUrls;
        }

    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(ProjectDetails projectDetails) {
        this.projectDetails = projectDetails;
    }

    public String getmProjectid() {
        return mProjectid;
    }

    public String getmProjectName() {
        return mProjectName;
    }

    public Double getmLatitude() {
        return mLatitude;
    }

    public Double getmLongitude() {
        return mLongitude;
    }

    public void setmProjectName(String mProjectName) {
        this.mProjectName = mProjectName;
    }

    public void setmLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setmLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }

}