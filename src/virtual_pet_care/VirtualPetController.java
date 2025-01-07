package virtual_pet_care;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class VirtualPetController {

    @FXML
    private Label petMoodLabel; // Label for pet's mood (emoji)

    @FXML
    private Label statusLabel; // Label for displaying pet's status

    @FXML
    private ProgressBar hungerBar, happinessBar, energyBar; // Progress bars for stats

    private double hunger = 0.8; // Initial hunger level (0 to 1)
    private double happiness = 0.8; // Initial happiness level (0 to 1)
    private double energy = 0.8; // Initial energy level (0 to 1)

    private Timeline statDecayTimer; // Timer for stats decay

    public void initialize() {
        // Start the stat decay timer (reduces stats every 5 seconds)
        statDecayTimer = new Timeline(new KeyFrame(Duration.seconds(5), e -> decayStats()));
        statDecayTimer.setCycleCount(Timeline.INDEFINITE);
        statDecayTimer.play();

        // Initial stats update
        updateStats();
    }

    @FXML
    public void feedPet() {
        hunger = Math.min(1.0, hunger + 0.2); // Increase hunger level
        updateStats();
    }

    @FXML
    public void playWithPet() {
        if (energy > 0.2) {
            happiness = Math.min(1.0, happiness + 0.2); // Increase happiness
            energy = Math.max(0.0, energy - 0.2); // Decrease energy
        } else {
            statusLabel.setText("Your pet is too tired to play!");
        }
        updateStats();
    }

    @FXML
    public void restPet() {
        energy = Math.min(1.0, energy + 0.3); // Increase energy
        hunger = Math.max(0.0, hunger - 0.1); // Slightly decrease hunger
        updateStats();
    }

    private void decayStats() {
        hunger = Math.max(0.0, hunger - 0.05); // Decrease hunger
        happiness = Math.max(0.0, happiness - 0.05); // Decrease happiness
        energy = Math.max(0.0, energy - 0.05); // Decrease energy

        updateStats();

        // Check for game over
        if (hunger == 0 || happiness == 0 || energy == 0) {
            gameOver();
        }
    }

    private void updateStats() {
        // Update progress bars
        hungerBar.setProgress(hunger);
        happinessBar.setProgress(happiness);
        energyBar.setProgress(energy);

        // Determine pet mood based on stats
        if (hunger < 0.3 || happiness < 0.3 || energy < 0.3) {
            petMoodLabel.setText("😢"); // Sad mood for low stats
        } else if (hunger > 0.7 && happiness > 0.7 && energy > 0.7) {
            petMoodLabel.setText("😊"); // Happy mood for high stats
        } else {
            petMoodLabel.setText("😐"); // Neutral mood for average stats
        }

        // Update status label
        statusLabel.setText("Hunger: " + Math.round(hunger * 100) + "%, Happiness: " +
                Math.round(happiness * 100) + "%, Energy: " + Math.round(energy * 100) + "%");
    }

    private void gameOver() {
        // Stop the stat decay timer
        statDecayTimer.stop();

        // Update the mood and status to indicate game over
        petMoodLabel.setText("💔"); // Broken heart emoji for game over
        statusLabel.setText("Oh no! Your pet ran away due to neglect.");
    }
}
