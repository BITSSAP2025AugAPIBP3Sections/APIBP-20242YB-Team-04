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
                System.out.println("🔄 Loading 50 sample events...");
                
                // Event 1
                createEvent(repository, "Tree Plantation Drive", "Join us for a green initiative to plant trees and make our city greener", "ORG123", "Environment", "Delhi", "Central Park", 28.6139, 77.2090, "2025-11-08T10:00:00+05:30", "2025-11-08T14:00:00+05:30", 100, 85);
                
                // Event 2
                createEvent(repository, "Tech for Good Workshop", "Technology workshop focused on building solutions for social good", "ORG456", "Technology", "Bangalore", "Tech Hub", 12.9716, 77.5946, "2025-11-11T09:00:00+05:30", "2025-11-11T17:00:00+05:30", 50, 92);
                
                // Event 3
                createEvent(repository, "Beach Cleanup Marathon", "Community event to clean our beautiful beaches", "ORG789", "Environment", "Mumbai", "Juhu Beach", 19.0990, 72.8260, "2025-11-09T06:30:00+05:30", "2025-11-09T11:30:00+05:30", 200, 76);
                
                // Event 4
                createEvent(repository, "Blood Donation Camp", "Save lives by donating blood at our organized camp", "ORG321", "Health", "Pune", "Red Cross Center", 18.5204, 73.8567, "2025-11-10T09:00:00+05:30", "2025-11-10T15:00:00+05:30", 150, 88);
                
                // Event 5
                createEvent(repository, "Elderly Care Volunteering", "Spend quality time with elderly citizens and make their day special", "ORG654", "Social Welfare", "Chennai", "SilverAge Home", 13.0827, 80.2707, "2025-11-14T10:00:00+05:30", "2025-11-14T16:00:00+05:30", 30, 79);
                
                // Event 6
                createEvent(repository, "Youth Education Drive", "Educational program for underprivileged youth", "ORG998", "Education", "Bangalore", "City Library", 12.9716, 77.5946, "2025-11-08T09:30:00+05:30", "2025-11-08T13:30:00+05:30", 80, 91);
                
                // Event 7
                createEvent(repository, "Animal Shelter Support", "Help care for rescued animals at our shelter", "ORG567", "Animal Welfare", "Mumbai", "PawSafe Center", 19.0760, 72.8777, "2025-11-12T08:00:00+05:30", "2025-11-12T12:00:00+05:30", 40, 88);
                
                // Event 8
                createEvent(repository, "Fundraising Marathon", "Marathon to raise funds for social causes", "ORG456", "Fundraising", "Delhi", "Nehru Stadium", 28.6139, 77.2090, "2025-11-18T07:00:00+05:30", "2025-11-18T12:00:00+05:30", 500, 82);
                
                // Event 9
                createEvent(repository, "Disaster Relief Volunteer Camp", "Training and volunteering for disaster management", "ORG321", "Disaster Relief", "Kolkata", "Hope Ground", 22.5726, 88.3639, "2025-11-20T08:00:00+05:30", "2025-11-20T16:00:00+05:30", 100, 86);
                
                // Event 10
                createEvent(repository, "Green Coding Hackathon", "Sustainable technology solutions hackathon", "ORG999", "Technology", "Pune", "CodeHub", 18.5204, 73.8567, "2025-11-23T09:00:00+05:30", "2025-11-23T20:00:00+05:30", 60, 94);
                
                // Event 11 - Bangalore
                createEvent(repository, "AI for Social Good", "Build AI solutions for community problems", "ORG123", "Technology", "Bangalore", "Innovation Hub", 12.9716, 77.5946, "2025-11-15T10:00:00+05:30", "2025-11-15T18:00:00+05:30", 75, 90);
                
                // Event 12 - Bangalore
                createEvent(repository, "Women in Tech Meetup", "Empowering women in technology sector", "ORG456", "Technology", "Bangalore", "CoWork Space", 12.9716, 77.5946, "2025-11-16T15:00:00+05:30", "2025-11-16T19:00:00+05:30", 60, 87);
                
                // Event 13 - Bangalore
                createEvent(repository, "Urban Farming Workshop", "Learn sustainable urban farming techniques", "ORG789", "Environment", "Bangalore", "Terrace Garden", 12.9716, 77.5946, "2025-11-17T08:00:00+05:30", "2025-11-17T12:00:00+05:30", 25, 78);
                
                // Event 14 - Bangalore
                createEvent(repository, "Startup Mentorship Program", "Connect with industry mentors and investors", "ORG321", "Technology", "Bangalore", "Startup Center", 12.9716, 77.5946, "2025-11-19T10:00:00+05:30", "2025-11-19T16:00:00+05:30", 100, 93);
                
                // Event 15 - Bangalore
                createEvent(repository, "Coding Bootcamp for Kids", "Teach programming to children aged 8-14", "ORG654", "Education", "Bangalore", "Learning Center", 12.9716, 77.5946, "2025-11-21T10:00:00+05:30", "2025-11-21T14:00:00+05:30", 40, 85);
                
                // Event 16 - Bangalore
                createEvent(repository, "Blockchain Workshop", "Understanding blockchain technology and applications", "ORG998", "Technology", "Bangalore", "Tech Park", 12.9716, 77.5946, "2025-11-22T14:00:00+05:30", "2025-11-22T17:00:00+05:30", 45, 89);
                
                // Event 17 - Bangalore
                createEvent(repository, "Mental Health Support Group", "Community mental wellness session", "ORG567", "Health", "Bangalore", "Wellness Center", 12.9716, 77.5946, "2025-11-24T16:00:00+05:30", "2025-11-24T18:00:00+05:30", 35, 81);
                
                // Event 18 - Bangalore
                createEvent(repository, "E-Waste Collection Drive", "Proper disposal of electronic waste", "ORG456", "Environment", "Bangalore", "Collection Point", 12.9716, 77.5946, "2025-11-25T07:00:00+05:30", "2025-11-25T13:00:00+05:30", 150, 84);
                
                // Event 19 - Bangalore
                createEvent(repository, "Cloud Computing Workshop", "Learn cloud technologies and DevOps", "ORG123", "Technology", "Bangalore", "Tech Academy", 12.9716, 77.5946, "2025-11-26T10:00:00+05:30", "2025-11-26T17:00:00+05:30", 55, 91);
                
                // Event 20 - Bangalore
                createEvent(repository, "Street Dog Vaccination Camp", "Free vaccination for street animals", "ORG789", "Animal Welfare", "Bangalore", "Vet Clinic", 12.9716, 77.5946, "2025-11-27T08:00:00+05:30", "2025-11-27T14:00:00+05:30", 100, 80);
                
                // Event 21 - Bangalore
                createEvent(repository, "Renewable Energy Seminar", "Future of clean and renewable energy", "ORG321", "Environment", "Bangalore", "Science Center", 12.9716, 77.5946, "2025-11-28T11:00:00+05:30", "2025-11-28T15:00:00+05:30", 90, 86);
                
                // Event 22 - Bangalore
                createEvent(repository, "Career Guidance for Youth", "Job readiness and skills development workshop", "ORG654", "Education", "Bangalore", "Youth Center", 12.9716, 77.5946, "2025-11-29T09:00:00+05:30", "2025-11-29T13:00:00+05:30", 75, 83);
                
                // Event 23 - Bangalore
                createEvent(repository, "Data Science Bootcamp", "Introduction to data analytics and ML", "ORG998", "Technology", "Bangalore", "Analytics Hub", 12.9716, 77.5946, "2025-11-30T10:00:00+05:30", "2025-11-30T18:00:00+05:30", 60, 92);
                
                // Event 24 - Bangalore
                createEvent(repository, "Community Kitchen Service", "Prepare meals for the underprivileged", "ORG567", "Social Welfare", "Bangalore", "Community Kitchen", 12.9716, 77.5946, "2025-12-01T06:00:00+05:30", "2025-12-01T10:00:00+05:30", 40, 77);
                
                // Event 25 - Bangalore
                createEvent(repository, "Cybersecurity Awareness", "Learn about online safety and security", "ORG456", "Technology", "Bangalore", "Cyber Center", 12.9716, 77.5946, "2025-12-02T09:00:00+05:30", "2025-12-02T16:00:00+05:30", 70, 88);
                
                // Event 26 - Mumbai
                createEvent(repository, "Marine Conservation Drive", "Protect ocean life and ecosystems", "ORG123", "Environment", "Mumbai", "Marine Drive", 18.9432, 72.8236, "2025-11-13T07:00:00+05:30", "2025-11-13T12:00:00+05:30", 120, 82);
                
                // Event 27 - Mumbai
                createEvent(repository, "Street Art Festival", "Community beautification project", "ORG789", "Arts & Culture", "Mumbai", "Bandra Wall", 19.0596, 72.8295, "2025-11-15T09:00:00+05:30", "2025-11-15T17:00:00+05:30", 80, 79);
                
                // Event 28 - Mumbai
                createEvent(repository, "Flood Relief Workshop", "Disaster preparedness training", "ORG321", "Disaster Relief", "Mumbai", "Relief Center", 19.0760, 72.8777, "2025-11-17T10:00:00+05:30", "2025-11-17T15:00:00+05:30", 60, 85);
                
                // Event 29 - Mumbai
                createEvent(repository, "Slum Education Initiative", "Teaching kids in underprivileged areas", "ORG654", "Education", "Mumbai", "Dharavi School", 19.0433, 72.8552, "2025-11-19T14:00:00+05:30", "2025-11-19T17:00:00+05:30", 50, 87);
                
                // Event 30 - Mumbai
                createEvent(repository, "Water Conservation Campaign", "Save water, save future", "ORG998", "Environment", "Mumbai", "Public Garden", 19.0176, 72.8561, "2025-11-21T08:00:00+05:30", "2025-11-21T12:00:00+05:30", 100, 81);
                
                // Event 31 - Delhi
                createEvent(repository, "Air Quality Monitoring", "Understand pollution levels in Delhi", "ORG567", "Environment", "Delhi", "Environment Office", 28.6139, 77.2090, "2025-11-13T10:00:00+05:30", "2025-11-13T14:00:00+05:30", 40, 83);
                
                // Event 32 - Delhi
                createEvent(repository, "Winter Clothing Donation", "Donate warm clothes for homeless", "ORG456", "Social Welfare", "Delhi", "Red Cross Center", 28.6139, 77.2090, "2025-11-15T09:00:00+05:30", "2025-11-15T18:00:00+05:30", 200, 89);
                
                // Event 33 - Delhi
                createEvent(repository, "Heritage Walk and Cleanup", "Clean historical monuments", "ORG123", "Environment", "Delhi", "India Gate", 28.6129, 77.2295, "2025-11-17T08:00:00+05:30", "2025-11-17T12:00:00+05:30", 80, 78);
                
                // Event 34 - Delhi
                createEvent(repository, "Road Safety Campaign", "Traffic awareness for children", "ORG789", "Education", "Delhi", "Traffic Park", 28.6139, 77.2090, "2025-11-19T10:00:00+05:30", "2025-11-19T13:00:00+05:30", 60, 75);
                
                // Event 35 - Delhi
                createEvent(repository, "Urban Gardening Workshop", "Grow your own food at home", "ORG321", "Environment", "Delhi", "Community Garden", 28.6139, 77.2090, "2025-11-21T09:00:00+05:30", "2025-11-21T12:00:00+05:30", 35, 80);
                
                // Event 36 - Pune
                createEvent(repository, "River Cleanup Initiative", "Clean the Mula-Mutha river", "ORG654", "Environment", "Pune", "River Bank", 18.5314, 73.8446, "2025-11-14T07:00:00+05:30", "2025-11-14T11:00:00+05:30", 120, 84);
                
                // Event 37 - Pune
                createEvent(repository, "Free Health Checkup Camp", "Medical screening for all age groups", "ORG998", "Health", "Pune", "Hospital Ground", 18.5204, 73.8567, "2025-11-16T08:00:00+05:30", "2025-11-16T14:00:00+05:30", 200, 90);
                
                // Event 38 - Pune
                createEvent(repository, "Music Therapy for Patients", "Healing through music and arts", "ORG567", "Health", "Pune", "Care Center", 18.5204, 73.8567, "2025-11-18T15:00:00+05:30", "2025-11-18T18:00:00+05:30", 30, 76);
                
                // Event 39 - Chennai
                createEvent(repository, "Tamil Language Revival", "Preserve classical Tamil literature", "ORG456", "Arts & Culture", "Chennai", "Cultural Center", 13.0827, 80.2707, "2025-11-15T09:00:00+05:30", "2025-11-15T13:00:00+05:30", 50, 82);
                
                // Event 40 - Chennai
                createEvent(repository, "Beach Sand Art Competition", "Create art for environment awareness", "ORG123", "Arts & Culture", "Chennai", "Marina Beach", 13.0475, 80.2824, "2025-11-17T06:00:00+05:30", "2025-11-17T10:00:00+05:30", 80, 77);
                
                // Event 41 - Chennai
                createEvent(repository, "Free Eye Checkup Camp", "Vision screening for all", "ORG789", "Health", "Chennai", "Eye Hospital", 13.0827, 80.2707, "2025-11-19T09:00:00+05:30", "2025-11-19T17:00:00+05:30", 100, 88);
                
                // Event 42 - Kolkata
                createEvent(repository, "Book Donation Drive", "Donate books to rural libraries", "ORG321", "Education", "Kolkata", "Book Fair Ground", 22.5726, 88.3639, "2025-11-16T10:00:00+05:30", "2025-11-16T18:00:00+05:30", 150, 85);
                
                // Event 43 - Kolkata
                createEvent(repository, "Durga Puja Cleanup", "Post-festival cleanup initiative", "ORG654", "Environment", "Kolkata", "Pandal Grounds", 22.5726, 88.3639, "2025-11-18T07:00:00+05:30", "2025-11-18T12:00:00+05:30", 200, 79);
                
                // Event 44 - Kolkata
                createEvent(repository, "Skill Development Workshop", "Vocational training for unemployed youth", "ORG998", "Education", "Kolkata", "Training Center", 22.5726, 88.3639, "2025-11-20T09:00:00+05:30", "2025-11-20T17:00:00+05:30", 70, 86);
                
                // Event 45 - Hyderabad
                createEvent(repository, "Tech Startup Summit", "Networking event for entrepreneurs", "ORG567", "Technology", "Hyderabad", "HITEC City", 17.4435, 78.3772, "2025-11-22T10:00:00+05:30", "2025-11-22T18:00:00+05:30", 150, 91);
                
                // Event 46 - Hyderabad
                createEvent(repository, "Food Distribution Drive", "Feed the homeless and needy", "ORG456", "Social Welfare", "Hyderabad", "Community Hall", 17.3850, 78.4867, "2025-11-24T07:00:00+05:30", "2025-11-24T11:00:00+05:30", 80, 84);
                
                // Event 47 - Jaipur
                createEvent(repository, "Heritage Craft Workshop", "Learn traditional Rajasthani crafts", "ORG123", "Arts & Culture", "Jaipur", "Craft Village", 26.9124, 75.7873, "2025-11-23T10:00:00+05:30", "2025-11-23T16:00:00+05:30", 45, 80);
                
                // Event 48 - Jaipur
                createEvent(repository, "Rural Education Outreach", "Teaching in rural government schools", "ORG789", "Education", "Jaipur", "Village School", 26.9124, 75.7873, "2025-11-25T09:00:00+05:30", "2025-11-25T15:00:00+05:30", 60, 87);
                
                // Event 49 - Ahmedabad
                createEvent(repository, "Solar Energy Workshop", "Installing solar panels in communities", "ORG321", "Environment", "Ahmedabad", "Energy Park", 23.0225, 72.5714, "2025-11-26T10:00:00+05:30", "2025-11-26T16:00:00+05:30", 50, 89);
                
                // Event 50 - Ahmedabad
                createEvent(repository, "Women Empowerment Program", "Skill training for women entrepreneurs", "ORG654", "Social Welfare", "Ahmedabad", "Women's Center", 23.0225, 72.5714, "2025-11-28T09:00:00+05:30", "2025-11-28T17:00:00+05:30", 70, 92);
                
                System.out.println("✅ Successfully loaded 50 events into database!");
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
