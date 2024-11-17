package vn.edu.hust.studentman

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.ActionBar.LayoutParams
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(var students: List<StudentModel>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
        val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
        val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
        val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_student_item,
            parent, false
        )
        return StudentViewHolder(itemView)
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]

        holder.textStudentName.text = student.studentName
        holder.textStudentId.text = student.studentId

        holder.imageEdit.setOnClickListener {
            val dialog = Dialog(it.context)
            dialog.setContentView(R.layout.edit_student_dialog)
            dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

            val edtName = dialog.findViewById<TextView>(R.id.edtName)
            val edtId = dialog.findViewById<TextView>(R.id.edtId)

            edtName.text = student.studentName
            edtId.text = student.studentId

            dialog.findViewById<Button>(R.id.btnSave).setOnClickListener {
                val studentName = edtName.text.toString()
                val studentId = edtId.text.toString()
                students.find { it.studentId == student.studentId }?.apply {
                    this.studentName = studentName
                    this.studentId = studentId
                }
                notifyItemChanged(position)
                dialog.dismiss()
            }

            dialog.show()
        }

        holder.imageRemove.setOnClickListener {
            students = students.toMutableList().apply { removeAt(position) }
            showSnackBar(student, holder, position)
            notifyItemRemoved(position)
        }
    }

    private fun showSnackBar(student: StudentModel, holder: StudentViewHolder, position: Int) {
        val snackBar = Snackbar.make(
            holder.itemView,
            "Student ${student.studentName} has been removed",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            students = students.toMutableList().apply { add(position, student) }
            notifyItemInserted(position)
        }
        snackBar.show()
    }
}