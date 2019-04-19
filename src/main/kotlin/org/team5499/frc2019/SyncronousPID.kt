// package org.team5499.frc2019
// /**
// * This class implements a PID Control Loop.
// *
// * Does all computation synchronously (i.e. the calculate() function must be called by the user from his own thread)
// */
// class SynchronousPIDF {
//   /**
//  * Get the Proportional coefficient
//  *
//  * @return proportional coefficient
//  */
//   var p:Double = 0.toDouble() // factor for "proportional" control
//   /**
//  * Get the Integral coefficient
//  *
//  * @return integral coefficient
//  */
//   var i:Double = 0.toDouble() // factor for "integral" control
//   /**
//  * Get the Differential coefficient
//  *
//  * @return differential coefficient
//  */
//   var d:Double = 0.toDouble() // factor for "derivative" control
//   /**
//  * Get the Feed forward coefficient
//  *
//  * @return feed forward coefficient
//  */
//   var f:Double = 0.toDouble() // factor for feed forward gain
//   private var m_maximumOutput = 1.0 // |maximum output|
//   private var m_minimumOutput = -1.0 // |minimum output|
//   private var m_maximumInput = 0.0 // maximum input - limit setpoint to
//   // this
//   private var m_minimumInput = 0.0 // minimum input - limit setpoint to
//   // this
//   private var m_continuous = false // do the endpoints wrap around? eg.
//   // Absolute encoder
//   private var m_prevError = 0.0 // the prior sensor input (used to compute
//   // velocity)
//   private var m_totalError = 0.0 // the sum of the errors for use in the
//   // integral calc
//   /**
//  * Returns the current setpoint of the PID controller
//  *
//  * @return the current setpoint
//  */
//   /**
//  * Set the setpoint for the PID controller
//  *
//  * @param setpoint
//  * the desired setpoint
//  */
//   var setpoint = 0.0
//   set(setpoint) {
//     if (m_maximumInput > m_minimumInput)
//     {
//       if (setpoint > m_maximumInput)
//       {
//         field = m_maximumInput
//       }
//       else if (setpoint < m_minimumInput)
//       {
//         field = m_minimumInput
//       }
//       else
//       {
//         field = setpoint
//       }
//     }
//     else
//     {
//       field = setpoint
//     }
//   }
//   /**
//  * Returns the current difference of the input from the setpoint
//  *
//  * @return the current error
//  */
//   var error = 0.0
//   private var m_result = 0.0
//   private var m_last_input = java.lang.Double.NaN
//   private var m_deadband = 0.0 // If the absolute error is less than
//   val state:String
//     get() {
//         var lState = ""
//         lState += "Kp: " + p + "\n"
//         lState += "Ki: " + i + "\n"
//         lState += "Kd: " + d + "\n"
//         return lState
//     }
//   val type:String
//     get() {
//         return "PIDController"
//     }
//   // deadband
//   // then treat error for the proportional
//   // term as 0
//   constructor() {}
//   /**
//  * Allocate a PID object with the given constants for P, I, D
//  *
//  * @param Kp
//  * the proportional coefficient
//  * @param Ki
//  * the integral coefficient
//  * @param Kd
//  * the derivative coefficient
//  */
//   constructor(Kp:Double, Ki:Double, Kd:Double) {
//     p = Kp
//     i = Ki
//     d = Kd
//     f = 0.0
//   }
//   /**
//  * Allocate a PID object with the given constants for P, I, D
//  *
//  * @param Kp
//  * the proportional coefficient
//  * @param Ki
//  * the integral coefficient
//  * @param Kd
//  * the derivative coefficient
//  * @param Kf
//  * the feed forward gain coefficient
//  */
//   constructor(Kp:Double, Ki:Double, Kd:Double, Kf:Double) {
//     p = Kp
//     i = Ki
//     d = Kd
//     f = Kf
//   }
//   /**
//  * Read the input, calculate the output accordingly, and write to the output. This should be called at a constant
//  * rate by the user (ex. in a timed thread)
//  *
//  * @param input
//  * the input
//  * @param dt
//  * time passed since previous call to calculate
//  */
//   fun calculate(input:Double, dt:Double):Double {
//     val ndt: Double
//     if (dt < 1E-6) {
//         ndt = 1E-6
//      } else ndt = 0.0
//     m_last_input = input
//     error = setpoint - input
//     if (m_continuous)
//     {
//       if (Math.abs(error) > (m_maximumInput - m_minimumInput) / 2)
//       {
//         if (error > 0)
//         {
//           error = error - m_maximumInput + m_minimumInput
//         }
//         else
//         {
//           error = error + m_maximumInput - m_minimumInput
//         }
//       }
//     }
//     if ((error * p < m_maximumOutput) && (error * p > m_minimumOutput))
//     {
//       m_totalError += error * ndt
//     }
//     else
//     {
//       m_totalError = 0.0
//     }
//     // Don't blow away m_error so as to not break derivative
//     val proportionalError = if (Math.abs(error) < m_deadband) 0.0 else error
//     m_result = ((p * proportionalError + i * m_totalError + d * (error - m_prevError) / ndt
//                  + f * setpoint))
//     m_prevError = error
//     if (m_result > m_maximumOutput)
//     {
//       m_result = m_maximumOutput
//     }
//     else if (m_result < m_minimumOutput)
//     {
//       m_result = m_minimumOutput
//     }
//     return m_result
//   }
//   /**
//  * Set the PID controller gain parameters. Set the proportional, integral, and differential coefficients.
//  *
//  * @param p
//  * Proportional coefficient
//  * @param i
//  * Integral coefficient
//  * @param d
//  * Differential coefficient
//  */
//   fun setPID(p:Double, i:Double, d:Double) {
//     this.p = p
//     this.i = i
//     this.d = d
//   }
//   /**
//  * Set the PID controller gain parameters. Set the proportional, integral, and differential coefficients.
//  *
//  * @param p
//  * Proportional coefficient
//  * @param i
//  * Integral coefficient
//  * @param d
//  * Differential coefficient
//  * @param f
//  * Feed forward coefficient
//  */
//   fun setPID(p:Double, i:Double, d:Double, f:Double) {
//     this.p = p
//     this.i = i
//     this.d = d
//     this.f = f
//   }
//   /**
//  * Return the current PID result This is always centered on zero and constrained the the max and min outs
//  *
//  * @return the latest calculated output
//  */
//   fun get():Double {
//     return m_result
//   }
//   /**
//  * Set the PID controller to consider the input to be continuous, Rather then using the max and min in as
//  * constraints, it considers them to be the same point and automatically calculates the shortest route to the
//  * setpoint.
//  *
//  * @param continuous
//  * Set to true turns on continuous, false turns off continuous
//  */
//   fun setContinuous(continuous:Boolean) {
//     m_continuous = continuous
//   }
//   fun setDeadband(deadband:Double) {
//     m_deadband = deadband
//   }
//   /**
//  * Set the PID controller to consider the input to be continuous, Rather then using the max and min in as
//  * constraints, it considers them to be the same point and automatically calculates the shortest route to the
//  * setpoint.
//  */
//   fun setContinuous() {
//     this.setContinuous(true)
//   }
//   /**
//  * Sets the maximum and minimum values expected from the input.
//  *
//  * @param minimumInput
//  * the minimum value expected from the input
//  * @param maximumInput
//  * the maximum value expected from the output
//  */
//   fun setInputRange(minimumInput:Double, maximumInput:Double) {
//     if (minimumInput > maximumInput)
//     {
//       throw Exception("Lower bound is greater than upper bound")
//     }
//     m_minimumInput = minimumInput
//     m_maximumInput = maximumInput
//     setpoint = setpoint
//   }
//   /**
//  * Sets the minimum and maximum values to write.
//  *
//  * @param minimumOutput
//  * the minimum value to write to the output
//  * @param maximumOutput
//  * the maximum value to write to the output
//  */
//   fun setOutputRange(minimumOutput:Double, maximumOutput:Double) {
//     if (minimumOutput > maximumOutput)
//     {
//       throw Exception("Lower bound is greater than upper bound")
//     }
//     m_minimumOutput = minimumOutput
//     m_maximumOutput = maximumOutput
//   }
//   /**
//  * Return true if the error is within the tolerance
//  *
//  * @return true if the error is less than the tolerance
//  */
//   fun onTarget(tolerance:Double):Boolean {
//     return m_last_input != java.lang.Double.NaN && Math.abs(m_last_input - setpoint) < tolerance
//   }
//   /**
//  * Reset all internal terms.
//  */
//   fun reset() {
//     m_last_input = java.lang.Double.NaN
//     m_prevError = 0.0
//     m_totalError = 0.0
//     m_result = 0.0
//     field = 0.0
//   }
//   fun resetIntegrator() {
//     m_totalError = 0.0
//   }
// }
