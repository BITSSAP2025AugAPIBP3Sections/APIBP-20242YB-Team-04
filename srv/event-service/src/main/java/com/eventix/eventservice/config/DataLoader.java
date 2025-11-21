package com.eventix.eventservice.config;

import com.eventix.eventservice.model.Event;
import com.eventix.eventservice.model.EventStatus;
import com.eventix.eventservice.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadEventData(EventRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                System.out.println("🔄 Loading 55 sample events...");
                
                // E206
                createEvent(repository, "Women's Health Awareness Camp", "Health awareness program for women", "ORG777", "Health", "Hyderabad", "City Wellness Center", 17.3850, 78.4867, "2025-11-15T09:00:00+05:30", "2025-11-15T14:00:00+05:30", 100, 83);
                
                // E207
                createEvent(repository, "Coding for Kids Bootcamp", "Technology workshop for children", "ORG888", "Education", "Bangalore", "Learning Arena", 12.9716, 77.5946, "2025-11-19T10:00:00+05:30", "2025-11-19T16:00:00+05:30", 50, 89);
                
                // E208
                createEvent(repository, "Mental Health Talk Session", "Mental wellness awareness session", "ORG432", "Health", "Delhi", "MindCare Hall", 28.6139, 77.2090, "2025-11-13T11:00:00+05:30", "2025-11-13T14:00:00+05:30", 80, 84);
                
                // E209
                createEvent(repository, "River Cleanup Awareness Drive", "Clean rivers for better tomorrow", "ORG567", "Environment", "Varanasi", "Ganga Ghat", 25.3176, 82.9739, "2025-11-16T06:00:00+05:30", "2025-11-16T10:00:00+05:30", 120, 78);
                
                // E210
                createEvent(repository, "Old Age Home Visit & Support", "Spend time with elderly citizens", "ORG654", "Social Welfare", "Chennai", "GoldenHome Care", 13.0827, 80.2707, "2025-11-22T10:00:00+05:30", "2025-11-22T15:00:00+05:30", 40, 80);
                
                // E211
                createEvent(repository, "Basic First Aid Training", "Learn life-saving first aid skills", "ORG321", "Health", "Kolkata", "Rescue Academy", 22.5726, 88.3639, "2025-11-25T09:00:00+05:30", "2025-11-25T13:00:00+05:30", 60, 87);
                
                // E212
                createEvent(repository, "Orphanage Donation & Fun Day", "Support orphan children with donations", "ORG900", "Social Welfare", "Mumbai", "Hope Orphanage", 19.0760, 72.8777, "2025-11-26T11:00:00+05:30", "2025-11-26T17:00:00+05:30", 100, 90);
                
                // E213
                createEvent(repository, "AI for Social Good Seminar", "Artificial Intelligence for social impact", "ORG888", "Technology", "Hyderabad", "Innovation Hub", 17.3850, 78.4867, "2025-11-24T09:30:00+05:30", "2025-11-24T16:00:00+05:30", 75, 93);
                
                // E214
                createEvent(repository, "Beach Conservation Talk", "Protecting coastal ecosystems", "ORG567", "Environment", "Goa", "Miramar Beach", 15.4909, 73.8278, "2025-11-12T08:00:00+05:30", "2025-11-12T11:00:00+05:30", 80, 79);
                
                // E215
                createEvent(repository, "Food Donation Drive", "Donate food to needy people", "ORG432", "Social Welfare", "Pune", "Community Center", 18.5204, 73.8567, "2025-11-28T10:00:00+05:30", "2025-11-28T14:00:00+05:30", 150, 88);
                
                // E216
                createEvent(repository, "Recycling Awareness Workshop", "Learn waste management and recycling", "ORG111", "Environment", "Delhi", "Green Hall", 28.6139, 77.2090, "2025-11-29T11:00:00+05:30", "2025-11-29T15:00:00+05:30", 70, 82);
                
                // E217
                createEvent(repository, "Women Empowerment Seminar", "Empowering women through education", "ORG222", "Social Welfare", "Jaipur", "Royal Convention Center", 26.9124, 75.7873, "2025-11-18T10:00:00+05:30", "2025-11-18T16:00:00+05:30", 100, 91);
                
                // E218
                createEvent(repository, "Stray Dog Feeding & Rescue Drive", "Help stray animals with food and care", "ORG567", "Animal Welfare", "Chandigarh", "City Plaza", 30.7333, 76.7794, "2025-11-21T07:30:00+05:30", "2025-11-21T11:30:00+05:30", 50, 85);
                
                // E219
                createEvent(repository, "Sports for All - Kids Camp", "Sports activities for children", "ORG998", "Education", "Bangalore", "PlayMax Ground", 12.9716, 77.5946, "2025-11-30T08:00:00+05:30", "2025-11-30T12:00:00+05:30", 80, 88);
                
                // E220
                createEvent(repository, "Community Gardening Initiative", "Create community green spaces", "ORG654", "Environment", "Chennai", "GreenView Park", 13.0827, 80.2707, "2025-11-27T06:00:00+05:30", "2025-11-27T10:00:00+05:30", 60, 81);
                
                // E221
                createEvent(repository, "Blood Pressure Screening Camp", "Free health screening for all", "ORG432", "Health", "Delhi", "Health Square", 28.6139, 77.2090, "2025-11-17T09:00:00+05:30", "2025-11-17T13:00:00+05:30", 120, 86);
                
                // E222
                createEvent(repository, "Tech Career Guidance for Students", "Career counseling for tech aspirants", "ORG999", "Education", "Pune", "Youth Center", 18.5204, 73.8567, "2025-11-28T13:00:00+05:30", "2025-11-28T17:00:00+05:30", 90, 92);
                
                // E223
                createEvent(repository, "Climate Change Awareness Talk", "Understanding climate change impacts", "ORG111", "Environment", "Ahmedabad", "Eco Hall", 23.0225, 72.5714, "2025-11-19T11:00:00+05:30", "2025-11-19T15:00:00+05:30", 100, 83);
                
                // E224
                createEvent(repository, "Book Donation Drive", "Donate books to rural libraries", "ORG777", "Education", "Hyderabad", "Readers Club", 17.3850, 78.4867, "2025-11-25T10:00:00+05:30", "2025-11-25T16:00:00+05:30", 150, 90);
                
                // E225
                createEvent(repository, "Emergency Relief Kit Packing", "Prepare relief kits for disasters", "ORG321", "Disaster Relief", "Kolkata", "Relief Warehouse", 22.5726, 88.3639, "2025-11-14T09:00:00+05:30", "2025-11-14T13:00:00+05:30", 80, 87);
                
                // E226
                createEvent(repository, "Wildlife Protection Awareness Camp", "Save endangered wildlife species", "ORG567", "Animal Welfare", "Nagpur", "GreenZoo Auditorium", 21.1458, 79.0882, "2025-11-22T09:00:00+05:30", "2025-11-22T13:00:00+05:30", 70, 82);
                
                // E227
                createEvent(repository, "Cancer Awareness Marathon", "Run for cancer awareness", "ORG432", "Health", "Mumbai", "Marine Drive", 18.9432, 72.8236, "2025-11-30T06:00:00+05:30", "2025-11-30T10:00:00+05:30", 200, 94);
                
                // E228
                createEvent(repository, "Senior Citizens Support Drive", "Support programs for elderly", "ORG654", "Social Welfare", "Noida", "CarePoint Home", 28.5355, 77.3910, "2025-11-21T10:00:00+05:30", "2025-11-21T14:00:00+05:30", 50, 80);
                
                // E229
                createEvent(repository, "Tree Adoption Event", "Adopt and nurture trees", "ORG111", "Environment", "Indore", "Eco Plaza", 22.7196, 75.8577, "2025-11-24T09:00:00+05:30", "2025-11-24T13:00:00+05:30", 100, 78);
                
                // E230
                createEvent(repository, "Open Source for Humanity Hackathon", "Code for social good", "ORG999", "Technology", "Bangalore", "Tech Park Arena", 12.9716, 77.5946, "2025-11-26T09:00:00+05:30", "2025-11-26T21:00:00+05:30", 120, 95);
                
                // E231
                createEvent(repository, "Child Safety Awareness Workshop", "Protect children from abuse", "ORG222", "Education", "Delhi", "Community Hall", 28.6139, 77.2090, "2025-11-27T11:00:00+05:30", "2025-11-27T15:00:00+05:30", 80, 89);
                
                // E232
                createEvent(repository, "Village School Support Program", "Educational support for rural schools", "ORG998", "Education", "Patna", "Harmony School", 25.5941, 85.1376, "2025-11-18T10:00:00+05:30", "2025-11-18T16:00:00+05:30", 60, 92);
                
                // E233
                createEvent(repository, "Water Conservation Drive", "Save water save life", "ORG111", "Environment", "Jaipur", "City Auditorium", 26.9124, 75.7873, "2025-11-14T10:00:00+05:30", "2025-11-14T14:00:00+05:30", 100, 81);
                
                // E234
                createEvent(repository, "Zero Hunger Campaign", "Fight against hunger", "ORG432", "Social Welfare", "Lucknow", "Unity Plaza", 26.8467, 80.9462, "2025-11-20T09:00:00+05:30", "2025-11-20T14:00:00+05:30", 150, 88);
                
                // E235
                createEvent(repository, "Tech Inclusivity Meetup", "Technology for everyone", "ORG888", "Technology", "Hyderabad", "DigiSphere", 17.3850, 78.4867, "2025-11-22T10:00:00+05:30", "2025-11-22T17:00:00+05:30", 90, 90);
                
                // E236
                createEvent(repository, "Forest Trail Cleanup", "Clean forest trails and paths", "ORG567", "Environment", "Mysore", "GreenTrail Woods", 12.2958, 76.6394, "2025-11-28T07:00:00+05:30", "2025-11-28T11:00:00+05:30", 80, 80);
                
                // E237
                createEvent(repository, "Mental Wellness for Youth", "Youth mental health support", "ORG777", "Health", "Mumbai", "City Hall", 19.0760, 72.8777, "2025-11-29T13:00:00+05:30", "2025-11-29T17:00:00+05:30", 70, 86);
                
                // E238
                createEvent(repository, "Coding for Social Impact Seminar", "Use coding to solve social problems", "ORG999", "Technology", "Pune", "NextGen Auditorium", 18.5204, 73.8567, "2025-11-30T09:00:00+05:30", "2025-11-30T16:00:00+05:30", 100, 93);
                
                // E239
                createEvent(repository, "Slum Area Education Support", "Teaching in underprivileged areas", "ORG432", "Education", "Mumbai", "Smile Center", 19.0760, 72.8777, "2025-11-23T10:00:00+05:30", "2025-11-23T16:00:00+05:30", 50, 89);
                
                // E240
                createEvent(repository, "Plastic-Free City Initiative", "Eliminate plastic waste", "ORG111", "Environment", "Hyderabad", "EcoZone Park", 17.3850, 78.4867, "2025-11-21T09:00:00+05:30", "2025-11-21T12:00:00+05:30", 100, 80);
                
                // E241
                createEvent(repository, "STEM for Girls Workshop", "Encouraging girls in STEM", "ORG222", "Education", "Bangalore", "Girls Innovation Hub", 12.9716, 77.5946, "2025-11-17T10:00:00+05:30", "2025-11-17T16:00:00+05:30", 60, 94);
                
                // E242
                createEvent(repository, "Pet Adoption Awareness Drive", "Adopt don't shop pets", "ORG567", "Animal Welfare", "Delhi", "PetZone Ground", 28.6139, 77.2090, "2025-11-19T09:00:00+05:30", "2025-11-19T13:00:00+05:30", 80, 84);
                
                // E243
                createEvent(repository, "Village Healthcare Assistance", "Medical support for rural areas", "ORG777", "Health", "Nashik", "CarePoint Clinic", 19.9975, 73.7898, "2025-11-27T08:00:00+05:30", "2025-11-27T14:00:00+05:30", 100, 85);
                
                // E244
                createEvent(repository, "National Cyber Safety Day", "Learn cyber security basics", "ORG888", "Technology", "Kolkata", "TechDefense Hall", 22.5726, 88.3639, "2025-11-29T09:00:00+05:30", "2025-11-29T15:00:00+05:30", 90, 91);
                
                // E245
                createEvent(repository, "School Renovation Volunteering", "Help renovate village schools", "ORG654", "Social Welfare", "Bhopal", "Sunshine School", 23.2599, 77.4126, "2025-11-26T09:00:00+05:30", "2025-11-26T16:00:00+05:30", 70, 88);
                
                // E246
                createEvent(repository, "Lakeside Cleanup Drive", "Clean lakes and water bodies", "ORG111", "Environment", "Udaipur", "Pichola Lake", 24.5854, 73.7125, "2025-11-28T07:00:00+05:30", "2025-11-28T11:00:00+05:30", 100, 79);
                
                // E247
                createEvent(repository, "Art Therapy for Children", "Heal through art", "ORG222", "Education", "Mumbai", "HappyArts Studio", 19.0760, 72.8777, "2025-11-30T13:00:00+05:30", "2025-11-30T17:00:00+05:30", 40, 86);
                
                // E248
                createEvent(repository, "Diabetes Awareness Walk", "Walk for diabetes awareness", "ORG321", "Health", "Ahmedabad", "City Walkway", 23.0225, 72.5714, "2025-11-15T07:00:00+05:30", "2025-11-15T11:00:00+05:30", 150, 87);
                
                // E249
                createEvent(repository, "Startup for Social Impact Meetup", "Social entrepreneurship networking", "ORG999", "Technology", "Chennai", "TechNest", 13.0827, 80.2707, "2025-11-19T09:00:00+05:30", "2025-11-19T15:00:00+05:30", 80, 93);
                
                // E250
                createEvent(repository, "Blood Donation Awareness Marathon", "Marathon for blood donation awareness", "ORG777", "Health", "Pune", "Riverside Track", 18.5204, 73.8567, "2025-11-22T06:00:00+05:30", "2025-11-22T10:00:00+05:30", 200, 85);
                
                // E251
                createEvent(repository, "Orphan Kids Sports Festival", "Sports day for orphan children", "ORG654", "Social Welfare", "Hyderabad", "KidsPlay Arena", 17.3850, 78.4867, "2025-11-24T10:00:00+05:30", "2025-11-24T15:00:00+05:30", 60, 89);
                
                // E252
                createEvent(repository, "College Coding Challenge", "Inter-college coding competition", "ORG888", "Technology", "Delhi", "TechVerse Hall", 28.6139, 77.2090, "2025-11-18T09:00:00+05:30", "2025-11-18T18:00:00+05:30", 100, 95);
                
                // E253
                createEvent(repository, "World Hygiene Day Camp", "Hygiene awareness program", "ORG321", "Health", "Bangalore", "WellCare Center", 12.9716, 77.5946, "2025-11-27T10:00:00+05:30", "2025-11-27T14:00:00+05:30", 80, 83);
                
                // E254
                createEvent(repository, "Green Living Lifestyle Workshop", "Sustainable living practices", "ORG111", "Environment", "Pune", "EcoPark Auditorium", 18.5204, 73.8567, "2025-11-29T11:00:00+05:30", "2025-11-29T16:00:00+05:30", 90, 82);
                
                // E255
                createEvent(repository, "Career Awareness at Government Schools", "Career guidance for students", "ORG998", "Education", "Chennai", "Govt School No. 18", 13.0827, 80.2707, "2025-11-26T09:00:00+05:30", "2025-11-26T13:00:00+05:30", 70, 89);
                
                // E256
                createEvent(repository, "Shelter Home Support Program", "Support homeless shelters", "ORG654", "Social Welfare", "Mumbai", "SafeHome Center", 19.0760, 72.8777, "2025-11-17T11:00:00+05:30", "2025-11-17T16:00:00+05:30", 50, 87);
                
                // E257
                createEvent(repository, "Forest Fire Prevention Awareness", "Prevent forest fires", "ORG111", "Environment", "Shimla", "Hillside Ground", 31.1048, 77.1734, "2025-11-20T10:00:00+05:30", "2025-11-20T14:00:00+05:30", 60, 80);
                
                // E258
                createEvent(repository, "Robotics for Kids Workshop", "Learn robotics and automation", "ORG888", "Technology", "Hyderabad", "RoboVerse Lab", 17.3850, 78.4867, "2025-11-23T10:00:00+05:30", "2025-11-23T17:00:00+05:30", 50, 92);
                
                // E259
                createEvent(repository, "Run for Clean Water", "Marathon for water conservation", "ORG432", "Environment", "Delhi", "India Gate", 28.6129, 77.2295, "2025-11-25T06:00:00+05:30", "2025-11-25T10:00:00+05:30", 250, 90);
                
                // E260
                createEvent(repository, "Child Rights Awareness Workshop", "Protect children's rights", "ORG222", "Social Welfare", "Kolkata", "Justice Hall", 22.5726, 88.3639, "2025-11-30T11:00:00+05:30", "2025-11-30T16:00:00+05:30", 80, 89);
                
                System.out.println("✅ Successfully loaded 55 events into database!");
            } else {
                System.out.println("ℹ️ Data already exists. Skipping data load.");
            }
        };
    }
    
    private void createEvent(EventRepository repository, String title, String description, 
                            String organizerId, String category, String city, String venue,
                            double latitude, double longitude, String startTime, String endTime, 
                            int capacity, int popularityScore) {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setOrganizerId(organizerId);
        event.setCategory(category);
        event.setCity(city);
        event.setVenue(venue);
        event.setLatitude(latitude);
        event.setLongitude(longitude);
        event.setStartTime(ZonedDateTime.parse(startTime));
        event.setEndTime(ZonedDateTime.parse(endTime));
        event.setCapacity(capacity);
        event.setSeatsAvailable(capacity); // Initially all seats available
        event.setStatus(EventStatus.PUBLISHED);
        
        repository.save(event);
    }
}
