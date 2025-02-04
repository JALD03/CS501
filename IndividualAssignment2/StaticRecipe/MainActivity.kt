package com.example.staticrecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Divider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import androidx.compose.ui.tooling.preview.Preview
import com.example.staticrecipe.ui.theme.StaticRecipeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StaticRecipeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Recipe(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Recipe(modifier: Modifier = Modifier){
    Card( shape = CutCornerShape(10.dp),
           colors = CardDefaults.cardColors( containerColor = Color.Gray),
            modifier = Modifier.padding(10.dp).fillMaxWidth()
    ){
        Column( modifier = Modifier.padding(10.dp) ){
        Box(){
            Row(){
                Image(
                    painter = painterResource(id = R.drawable.download),
                    contentDescription = "Chicken Image",
                    modifier = Modifier.size(200.dp)
                )
                Text(text = "Maple Chicken",
                    fontSize = 30.sp,
                    color = Color.White)
            }
        }
            Divider(color = Color.White)
            Spacer( modifier = Modifier.height(10.dp))
            Text( text = "Ingredients",
                fontSize = 15.sp,
                color = Color.White)
            Text( text = "- 3 (6-ounce) Chicken breast halves (skinless & boneless)\n" +
                    "- 1/4 tsp Black pepper\n" +
                    "- 1/4 tsp Kosher salt\n" +
                    "- 2-3 tsp Olive oil\n" +
                    "- 1/4 cup Chicken broth\n" +
                    "- 1/4 cup Maple syrup\n" +
                    "- 1/2 tsp Thyme, dried\n" +
                    "- 3 tsp Garlic, chopped (or cloves)\n" +
                    "- 1 tbsp Cider vinegar\n" +
                    "- 1 tbsp Dijon mustard",
                color = Color.White)
            Divider(color = Color.White)
            Text( text = "Instructions",
                fontSize = 15.sp,
                color = Color.White)
            Text( text = "1. Preheat oven to 400°.\n" +
                    "2. Sprinkle chicken with pepper and salt.\n" +
                    "3. Heat olive a large ovenproof skillet over medium-high heat. Add chicken to pan and sauté 2 minutes on each side or until browned. Remove chicken from pan.\n" +
                    "4. Add broth, syrup, thyme, and garlic to pan.  Bring to a boil, scraping pan to loosen browned bits and cook for 2 minutes, stirring frequently. Add vinegar and mustard and stir constantly for an additional minute.\n" +
                    "5. Return chicken to pan, and spoon sauce over chicken. Bake at 400° for 12 minutes.\n" +
                    "6. Remove chicken from pan and let stand around 2 minutes.  Spoon sauce over chicken and serve.",
                color = Color.White)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun RecipePreview() {
    StaticRecipeTheme {
        Recipe()
    }
}